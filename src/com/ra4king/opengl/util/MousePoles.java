package com.ra4king.opengl.util;

import com.ra4king.opengl.util.math.Matrix4;
import com.ra4king.opengl.util.math.Quaternion;
import com.ra4king.opengl.util.math.Vector3;

public class MousePoles {
	private static abstract class ViewProvider {
		public abstract Matrix4 calcMatrix();
	}
	
	private enum MouseButtons {
		LEFT_BUTTON,
		MIDDLE_BUTTON,
		RIGHT_BUTTON,
	}
	
	private enum MouseModifiers {
		KEY_SHIFT,
		KEY_CTRL,
		KEY_ALT,
	}
	
	private static class ObjectData {
		private Vector3 position;
		private Quaternion orientation;
		
		public ObjectData() {
			position = new Vector3();
			orientation = new Quaternion();
		}
		
		public ObjectData(Vector3 v, Quaternion q) {
			position = v;
			orientation = q;
		}
	}
	
	public static class ObjectPole {
		
	}
	
	private static class ViewData {
		private Vector3 targetPos;
		private Quaternion orient;
		private float radius;
		private float degSpinRotation;
		
		public ViewData() {
			targetPos = new Vector3();
			orient = new Quaternion();
		}
		
		public ViewData(Vector3 v, Quaternion q, float r, float d) {
			targetPos = v;
			orient = q;
			radius = r;
			degSpinRotation = d;
		}
	}
	
	private static class ViewScale {
		private float minRadius;
		private float maxRadius;
		private float largeRadiusDelta;
		private float smallRadiusDelta;
		private float largePosOffset;
		private float smallPosOffset;
		private float rotationScale;
		
		public ViewScale() {}
		
		public ViewScale(float min, float max, float large, float small, float largePos, float smallPos, float rot) {
			minRadius = min;
			maxRadius = max;
			largeRadiusDelta = large;
			smallRadiusDelta = small;
			largePosOffset = largePos;
			smallPosOffset = smallPos;
			rotationScale = rot;
		}
	}
	
	public static class ViewPole {
		
	}
}
