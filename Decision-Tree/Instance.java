import java.util.ArrayList;
import java.util.List;


public class Instance {
	public Integer label;
	public List<Integer> attributes = null;

	public void addAttribute(Integer i) {
		if (attributes == null) {
			attributes = new ArrayList<Integer>();
		}
		attributes.add(i);
	}
}
