import java.util.ArrayList;
import java.util.List;


public class DecTreeNode {
	Integer label; //for 
	Integer attribute;
	Integer parentAttributeValue; // if is the root, set to "-1"
	boolean terminal;
	List<DecTreeNode> children;

	DecTreeNode(Integer _label, Integer _attribute, Integer _parentAttributeValue, boolean _terminal) {
		label = _label;
		attribute = _attribute;
		parentAttributeValue = _parentAttributeValue;
		terminal = _terminal;
		if (_terminal) {
			children = null;
		} else {
			children = new ArrayList<DecTreeNode>();
		}
	}

	/**
	 * Add child to the node
	 */
	public void addChild(DecTreeNode child) {
		if (children != null) {
			children.add(child);
		}
	}
}
