package gdi1sokoban.base;

import java.util.HashMap;
import java.util.Map;

/**
 * This resource manager is somehow similar to the singleton pattern, the
 * difference is that instead of a single instance this manager contains a
 * collection of different resources, each group of resources whose
 * ResourceDescriptor returns the same ID are treated as a singleton.
 */
public class ResourceManager<T> {
	
	HashMap<String, Resource> _resources = new HashMap<String, Resource>();
	
	public class Resource {
		private T _t;
		private String _id;
		private int _nReferences = 1;
		
		private Resource(T t, String id) {
			_t = t;
			_id = id;
		}
		
		public T getInstance() {
			return _t;
		}
		
		private void reference() {
			_nReferences++;
		}
		
		private int getReferenceCount() {
			return _nReferences;
		}
		
		private void setReferenceCount(int count) {
			_nReferences = count;
		}
		
		protected void finalize() throws Throwable {
			try {
				_nReferences--;
				if (_nReferences <= 0) {
					ResourceManager.this.release(_id);
				}
			} finally {
				super.finalize();
			}
		}
	}
	
	protected ResourceManager() {}
	
	public Resource getInstance(ResourceDescriptor<T> rd) throws Exception {
		String resourceID = rd.getId();
		Resource resource = _resources.get(resourceID);
		
		if (resource == null) {
			
			T t = null;
			t = rd.build();
			
			resource = new Resource(t, resourceID);
			_resources.put(resourceID, resource);
		} else {
			resource.reference();
		}
		return resource;
	}
	
	public void clear() {
		for (Map.Entry<String, Resource> iResource : _resources.entrySet()) {
			if (iResource.getValue().getReferenceCount() == 0)
				_resources.put(iResource.getKey(), null);
		}
	}
	
	public void flush() {
		for (Map.Entry<String, Resource> iResource : _resources.entrySet()) {
			_resources.get(iResource.getKey()).setReferenceCount(0);
			_resources.put(iResource.getKey(), null);
		}
	}
	
	private void release(String resourceID) {
		_resources.put(resourceID, null);
	}
}
