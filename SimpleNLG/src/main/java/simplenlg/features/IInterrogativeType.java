package simplenlg.features;

public interface IInterrogativeType {

	public IInterrogativeType getInstance(String value);
	public String getQuestion();
	public boolean isObject(Object type);
	public boolean isIndirectObject(Object type);
}
