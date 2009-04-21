package gdi1sokoban.base;

/**
 * Resource management requires information about how objects are identified 
 * and, given the case a resource width the given identifier does not exist,
 * build. This ResourceDescriptor-class provides this functionality.
 */
public abstract class ResourceDescriptor<T> {
	abstract public String getId();
	
	abstract public T build() throws Exception;
}