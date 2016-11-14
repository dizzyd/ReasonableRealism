package com.draco18s.hardlib.internal;

import com.draco18s.hardlib.math.HashUtils;

public class ChunkCoords {
	public int dimID;
	public int posX;
	public int posY;
	public int posZ;
	private int fHashCode;
	
	public ChunkCoords(int dim, int x, int y, int z) {
		dimID = dim;
		posX = x;
		posY = y;
		posZ = z;
		hashCode();
	}
	
	@Override
	public int hashCode() {
		if (fHashCode == 0) {
		      int result = HashUtils.SEED;
		      result = HashUtils.hash(result, dimID);
		      result = HashUtils.hash(result, posX);
		      result = HashUtils.hash(result, posY);
		      result = HashUtils.hash(result, posZ);
		      fHashCode = result;
		}
		return fHashCode;
    }
	
	@Override
	public boolean equals(Object aThat) {
		ChunkCoords that = (ChunkCoords) aThat;
		return that.dimID == dimID && that.posX == posX && that.posY == posY && that.posZ == posZ;    
	}
	
	@Override
	public String toString() {
		return "["+dimID+"]("+posX+","+posY+","+posZ+")";
	}
}