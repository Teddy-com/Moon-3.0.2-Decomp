/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.util.vector.Matrix4f
 */
package net.minecraftforge.client.model;

import net.minecraft.util.EnumFacing;
import org.lwjgl.util.vector.Matrix4f;

public interface ITransformation {
    public Matrix4f getMatrix();

    public EnumFacing rotate(EnumFacing var1);

    public int rotate(EnumFacing var1, int var2);
}

