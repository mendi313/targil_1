package geometries;

import primitives.Point3D;
import primitives.*;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

public class Plane extends Geometry {
    Point3D _p;
    Vector _normal;

    /***************contractors***********/

    /**
     * contractors that build the plane by 3 points
     *
     * @param emissionLight the emissionLight of the Plane
     * @param material      the attenuation
     * @param p1            first point for creating a Plane
     * @param p2            second point for creating a Plane
     * @param p3            third point for creating a Plane
     */
    public Plane(Color emissionLight, Material material, Point3D p1, Point3D p2, Point3D p3) {
        super(emissionLight, material);
        _p = new Point3D(p1);
        Vector U = new Vector(p1, p2);
        Vector V = new Vector(p1, p3);
        Vector N = U.crossProduct(V);
        N.normalize();
        _normal = N;
    }

    /**
     * contractors that build the plane by 3 points,
     * with default Values for the kd, ks and nShininess.
     *
     * @param emissionLight the emissionLight of the Plane
     * @param p1            first point for creating a Plane
     * @param p2            second point for creating a Plane
     * @param p3            third point for creating a Plane
     */
    public Plane(Color emissionLight, Point3D p1, Point3D p2, Point3D p3) {
        this(emissionLight, new Material(0, 0, 0), p1, p2, p3);
    }

    /**
     * contractors that build the plane by 3 points,
     * with default Values for the kd, ks, emissionLight, and nShininess.
     *
     * @param p1 first point for creating a Plane
     * @param p2 second point for creating a Plane
     * @param p3 third point for creating a Plane
     */
    public Plane(Point3D p1, Point3D p2, Point3D p3) {
        this(Color.BLACK, p1, p2, p3);
    }

    /**
     * contractors that build the plane by point and normal vector
     *
     * @param _p      first param
     * @param _normal second param
     */
    public Plane(Point3D _p, Vector _normal) {
        super(Color.BLACK, new Material(0, 0, 0));
        this._p = new Point3D(_p);
        this._normal = new Vector(_normal);
    }

    @Override
    public Vector getNormal(Point3D p) {
        return _normal;
    }

    //because polygon

    /**
     * func that return normal to the plane
     *
     * @return normal vector
     */
    public Vector getNormal() {
        return getNormal(null);
    }

    /**
     * func to find the Intersections point between the ray and the Plane
     *
     * @param ray ray pointing toward a Geometry
     * @return List<GeoPoint> with the Intersections point
     */
    @Override
    public List<GeoPoint> findIntersections(Ray ray, double maxDistance) {
        Vector p0Q;
        try {
            p0Q = _p.subtract(ray.getPoint());
        } catch (IllegalArgumentException e) {
            return null; // ray starts from point Q - no intersections
        }

        double nv = _normal.dotProduct(ray.getDirection());
        if (isZero(nv)) { // ray is parallel to the plane - no intersections
            return null;
        }
        double t = alignZero(_normal.dotProduct(p0Q) / nv);
        double tdist = alignZero(maxDistance - t);

        if ((t <= 0) || (tdist <= 0)) {
            return null;
        } else {
            return List.of(new GeoPoint(this, ray.getTargetPoint(t)));
        }
    }
}