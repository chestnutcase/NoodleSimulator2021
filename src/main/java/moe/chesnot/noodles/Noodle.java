package moe.chesnot.noodles;

import org.joml.Vector3f;
import org.lwjglb.engine.IGameItem;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.Renderable;
import org.lwjglb.engine.graph.Texture;

import java.util.*;

public class Noodle implements IGameItem {
    // physics simulation components
    private final NoodleCurveFactory<?> noodleCurveFactory;
    private final List<NoodlePhysicsPoint> points;
    private final List<NoodleCurve> curves;
    private final List<NoodleCrossSection> crossSections;
    private final Mesh sweptSurfaceMesh;

    // noodle modelling parameters
    private final int NUM_SIDES;
    private final float THICKNESS;

    // gameitem components
    private Vector3f position = new Vector3f();
    private float scale = 1;
    private Vector3f rotation = new Vector3f();

    public Noodle(NoodleCurveFactory<?> noodleCurveFactory, Texture texture, int numSides, float thickness){
        this(new Vector3f[] {
                new Vector3f(0, 0,0),
                new Vector3f(0, 0, 10f)
        }, noodleCurveFactory, texture, numSides, thickness);
    }

    public Noodle(Vector3f[] points, NoodleCurveFactory<?> noodleCurveFactory, Texture texture, int numSides, float thickness){
        this.noodleCurveFactory = noodleCurveFactory;
        this.points = new LinkedList<NoodlePhysicsPoint>();
        this.curves = new LinkedList<NoodleCurve>();
        this.crossSections = new LinkedList<NoodleCrossSection>();
        NUM_SIDES = numSides;
        THICKNESS = thickness;
        for(int i = 0; i < points.length / noodleCurveFactory.getFitCount(); i++){
            // i is the curve number
            // a noodle is subdivided into many smaller curves depending on the curve factory
            this.points.add(new NoodlePhysicsPoint(points[i]));
            ArrayList<Vector3f> curvePoints = new ArrayList<>();
            for(int j = 0; j < noodleCurveFactory.getFitCount(); j++){
                curvePoints.add(points[(i* noodleCurveFactory.getFitCount()) + j]);
            }
            NoodleCurve curve = noodleCurveFactory.fit(curvePoints);
            this.curves.add(curve);
        }
        int sweptSurfaceVertexCount = 0;
        for(NoodleCurve curve: this.curves){
            float t = 0;
            float step = 0.1f;
            while(t <= 1.0f){
                Vector3f position = curve.getPosition(t);
                Vector3f normal = curve.getTangent(t);
                NoodleCrossSection cs = new PolygonCrossSection(position, THICKNESS, normal, NUM_SIDES, texture);
                this.crossSections.add(cs);
                cs.allocate();
                sweptSurfaceVertexCount += NUM_SIDES;
                t += step;
            }
        }
        // create the swept surface mesh
        // number of unique position vertices = total number of points in all cross sections
        float[] sweptSurfaceVertexPositions = new float[sweptSurfaceVertexCount * 3];
        Iterator<NoodleCrossSection> csi = this.crossSections.iterator();
        int _vertexCount = 0;
        while(csi.hasNext()){
            NoodleCrossSection next = csi.next();
            for (Vector3f point : next.getPoints()) {
                sweptSurfaceVertexPositions[(_vertexCount * 3) + 0] = point.x;
                sweptSurfaceVertexPositions[(_vertexCount * 3) + 1] = point.y;
                sweptSurfaceVertexPositions[(_vertexCount * 3) + 2] = point.z;
                _vertexCount++;
            }
        }
        // for every pair of n-sided cross sections, I will have n faces
        // for every face, i will have 2 triangles
        int[] sweptSurfaceVertexIndices = new int[(this.crossSections.size() - 1) * NUM_SIDES * 2 * 3];
        ListIterator<NoodleCrossSection> forwardSectionIterator = this.crossSections.listIterator(1);
        ListIterator<NoodleCrossSection> rearSectionIterator = this.crossSections.listIterator(0);
        int _indexArrayIndex = 0;
        while(forwardSectionIterator.hasNext()){
            NoodleCrossSection forwardSection = forwardSectionIterator.next();
            NoodleCrossSection rearSection = rearSectionIterator.next();
            for(int i = 0; i < forwardSection.getPoints().size(); i++){
                int a = (forwardSectionIterator.previousIndex() * NUM_SIDES) + i;
                int b = (forwardSectionIterator.previousIndex() * NUM_SIDES) + ((i + 1) % NUM_SIDES);
                int c = (rearSectionIterator.previousIndex() * NUM_SIDES) + i;
                int d = (rearSectionIterator.previousIndex() * NUM_SIDES) + ((i + 1) % NUM_SIDES);
                sweptSurfaceVertexIndices[_indexArrayIndex] = a;
                sweptSurfaceVertexIndices[_indexArrayIndex + 1] = b;
                sweptSurfaceVertexIndices[_indexArrayIndex + 2] = c;
                _indexArrayIndex += 3;
                sweptSurfaceVertexIndices[_indexArrayIndex] = c;
                sweptSurfaceVertexIndices[_indexArrayIndex + 1] = d;
                sweptSurfaceVertexIndices[_indexArrayIndex + 2] = b;
                _indexArrayIndex += 3;
            }
        }
        float[] textCoords = new float[sweptSurfaceVertexCount * 2];
        // TODO: texture mapping
//        for (int i = 0; i < (sweptSurfaceVertexCount / NUM_SIDES) - 1; i++) {
//            textCoords[(i*NUM_SIDES)*2] = 0;
//            textCoords[(i*NUM_SIDES)*2] = 0;
//        }
        sweptSurfaceMesh = new Mesh(sweptSurfaceVertexPositions, textCoords, sweptSurfaceVertexIndices, texture);
        sweptSurfaceMesh.allocate();
    }

    @Override
    public Vector3f getPosition() {
        return new Vector3f(position);
    }

    @Override
    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    @Override
    public float getScale() {
        return scale;
    }

    @Override
    public void setScale(float scale) {
        this.scale = scale;
    }

    @Override
    public Vector3f getRotation() {
        return new Vector3f(rotation);
    }

    @Override
    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    @Override
    public Iterable<Renderable> getRenderables() {
        LinkedList<Renderable> l = new LinkedList<Renderable>(this.crossSections);
        l.add(this.sweptSurfaceMesh);
        return l;
    }
}
