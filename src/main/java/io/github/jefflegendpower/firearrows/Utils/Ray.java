package io.github.jefflegendpower.firearrows.Utils;

import net.minecraft.server.v1_16_R3.AxisAlignedBB;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;

import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Ray {

    public double startX, startY, startZ;
    public double endX, endY, endZ;
    public Set<Entity> entitySet;

    public Ray(double startX, double startY, double startZ, double endX, double endY, double endZ) {
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.endX = endX;
        this.endY = endY;
        this.endZ = endZ;
        entitySet = new HashSet<Entity>();
    }

    public void removeNonCollidingAlongXZPlane() {
        Iterator<Entity> entityIterator = entitySet.iterator();
        while (entityIterator.hasNext()) {
            AxisAlignedBB aabb = ((CraftEntity) entityIterator.next()).getHandle().getBoundingBox();

            double rectX = aabb.minX, rectXLength = aabb.maxX - rectX;
            double rectZ = aabb.minZ, rectZLength = aabb.maxZ - rectZ;
            Rectangle2D.Double entityRectangle = new Rectangle2D.Double(rectX, rectZ, rectXLength, rectZLength);

            boolean collided = entityRectangle.intersectsLine(startX, startZ, endX, endZ);

            if (!collided) entityIterator.remove();
        }
    }

    public void removeNonCollidingAlongXYPlane() {
        Iterator<Entity> entityIterator = entitySet.iterator();
        while (entityIterator.hasNext()) {
            AxisAlignedBB aabb = ((CraftEntity) entityIterator.next()).getHandle().getBoundingBox();

            double rectX = aabb.minX, rectXLength = aabb.maxX - rectX;
            double rectY = aabb.minY, rectYLength = aabb.maxY - rectY;

            Rectangle2D.Double entityRectangle = new Rectangle2D.Double(rectX, rectY, rectXLength, rectYLength);

            boolean collided = entityRectangle.intersectsLine(startX, startY, endX, endY);

            if (!collided) entityIterator.remove();
        }
    }

    public void removeNonCollidingAlongZYPlane() {
        Iterator<Entity> entityIterator = entitySet.iterator();
        while (entityIterator.hasNext()) {
            AxisAlignedBB aabb = ((CraftEntity) entityIterator.next()).getHandle().getBoundingBox();

            double rectZ = aabb.minZ, rectZLength = aabb.maxZ - rectZ;
            double rectY = aabb.minY, rectYLength = aabb.maxY - rectY;
            Rectangle2D.Double entityRectangle = new Rectangle2D.Double(rectZ, rectY, rectZLength, rectYLength);

            boolean collided = entityRectangle.intersectsLine(startZ, startY, endZ, endY);


            if (!collided) entityIterator.remove();
        }
    }

    public void removeAllNonColliding() {
        removeNonCollidingAlongXZPlane();
        removeNonCollidingAlongXYPlane();
        removeNonCollidingAlongZYPlane();
    }

    public Set<Entity> getEntities() {
        return entitySet;
    }
}
