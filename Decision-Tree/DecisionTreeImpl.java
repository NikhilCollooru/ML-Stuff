// NIKHIL GUPTA COLLOORU

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class DecisionTreeImpl extends DecisionTree {
	private DecTreeNode root;
	private List<String> labels; // ordered list of class labels
	private List<String> attributes; // ordered list of attributes
	private Map<String, List<String>> attributeValues; // map to ordered
														// discrete values taken
														// by attributes
    private List<AccNode> pr_tr_list = new ArrayList<AccNode>();
	
	DecisionTreeImpl() {

	}


	DecisionTreeImpl(DataSet train) {

		this.labels = train.labels;
		this.attributes = train.attributes;
		this.attributeValues = train.attributeValues;

		int inst_count = train.instances.size();
		int labl_count = this.labels.size();
		int labe[] = new int[labl_count];
		int max =0,i,j=0;
		for( i=0; i < inst_count; i++)
		{
		  for(j=0; j<labl_count; j++)
		  {
		    if( train.instances.get(i).label == j)
			{
			  labe[j]++;
			  if(labe[j] > labe[max])
			     max = j;
			}
		  }
		}
		
		List<Integer> attrbts = new ArrayList<Integer>();
		

		
		for(i=0; i < (this.attributes.size()) ; i++)     
		  attrbts.add(i);
		this.root = buildtree(train.instances,attrbts,max,-1);
	}

	DecTreeNode buildtree(List<Instance> instances, List<Integer> attrs,Integer def_label_no, Integer parentattributeValue)
	{
	  int i=0,j=0;
	  double entropy_labels = Ent_labels(instances);    //entropy
	  
	  int no_of_labels = this.labels.size();
	  int inst_per_label[] = new int[no_of_labels];
	  int mx_count=0, maj_lab=9999;
	  for ( i=0; i<instances.size(); i++)
	  {
	    inst_per_label[instances.get(i).label]++;
		if(inst_per_label[instances.get(i).label] == mx_count)
		{
		  if(instances.get(i).label< maj_lab)
		  {
		    mx_count = inst_per_label[instances.get(i).label];
		    maj_lab = instances.get(i).label;
		  }
		}
		else if(inst_per_label[instances.get(i).label] > mx_count )
		{
		 mx_count = inst_per_label[instances.get(i).label];
		 maj_lab = instances.get(i).label;
		}
	  }
	   
	  if(instances.size() ==0)
	  {
	    DecTreeNode node = new DecTreeNode( def_label_no, 99999, parentattributeValue,true);
	    return node;
	  }
	  else if ( attrs.size() == 0)
	  {
		DecTreeNode node = new DecTreeNode( maj_lab, 99999, parentattributeValue,true);
		return node;
	  }
	  else if ( entropy_labels == 0)
	  {
	    Instance example = instances.get(0);
	    DecTreeNode node = new DecTreeNode( example.label, 99999, parentattributeValue,true);
		return node;
	  }
	  else
	  { 
	    double entropy_given_attribute[] = new double[attrs.size()];
	    double info_gain[] = new double[attrs.size()];
	    double max_info_gain=0.0;
	    int attr_with_max_info_gain=999;
	 
	  
	    for(i=0 ; i< attrs.size() ; i++)
	    {
	      entropy_given_attribute[i] = Ent_giv_attr(instances, attrs.get(i));
	      info_gain[i] = entropy_labels - entropy_given_attribute[i];
		  if(info_gain[i] == max_info_gain )
		  {
		   if(i < attr_with_max_info_gain)
		   {
		    attr_with_max_info_gain = i;
		    max_info_gain = info_gain[i];
		   }
		  }
		  else if(info_gain[i] > max_info_gain )
		  {
		    attr_with_max_info_gain = i;
		    max_info_gain = info_gain[i];
		  }
	    }
		int no_of_children;
        int	actual_attr;
		String attr_name;
		actual_attr  = attrs.get(attr_with_max_info_gain);
		attr_name = this.attributes.get(actual_attr);
	
	    no_of_children = (this.attributeValues.get(attr_name)).size();
		
	    DecTreeNode node = new DecTreeNode(maj_lab, actual_attr, parentattributeValue, false);
         
		List<List<Instance>> new_insts = new ArrayList<List<Instance>>();
	    List<Integer> new_attrs = new ArrayList<Integer>(attrs);
		
		new_attrs.remove(attr_with_max_info_gain);
		
	    for( i=0 ; i < instances.size() ; i++)
		{
		  Instance cur_instance = instances.get(i);
		  for(j=0; j< no_of_children; j++)
		  { 
		    if(i==0)
			  new_insts.add(new ArrayList<Instance>());
		    if( (cur_instance.attributes).get(actual_attr) == j)
			  new_insts.get(j).add(cur_instance);
		  }
		}

		List<DecTreeNode> childs = new ArrayList<DecTreeNode>(no_of_children);
		
		for( i=0 ; i < no_of_children ; i++)
		{  
		  DecTreeNode child = buildtree( new_insts.get(i),new_attrs,maj_lab,i);
		  childs.add(child);
          node.children = childs;
		}
	    return node;
	  }
    }

	double Ent_giv_attr(List<Instance> instances, Integer attr_no)
	{
	  int no_labels = this.labels.size();
	  List<String> attrvalues = new ArrayList<String>(this.attributeValues.get(this.attributes.get(attr_no)));
	  int inst_count_with_given_attrvalue[][] = new int[no_labels][attrvalues.size()];
	  int i=0,j=0;
	  int inst_count = instances.size();
	  double entropy=0.0,prop;
	  int labe[] = new int[attrvalues.size()]; // number of instances with a particular attribute value
	  for( i=0; i < inst_count; i++)
	  {
		for(j=0; j<no_labels; j++)
		{
		  if( instances.get(i).label == j)
		  {
		    inst_count_with_given_attrvalue[j][instances.get(i).attributes.get(attr_no)]++;
			labe[instances.get(i).attributes.get(attr_no)]++;
		  }	
		}
	  }
	  double net_entropy =0.0;
	  for(i=0; i< attrvalues.size(); i++)
	  {
	    entropy = 0.0;
	    for(j=0; j< this.labels.size(); j++)
	    {
	      prop = ((double)inst_count_with_given_attrvalue[j][i])/(labe[i]);
		  if(prop>0 && prop<1)
		     entropy = entropy + (-1)*prop * (Math.log(prop)/Math.log(2));
	    } 
	    prop = ((double)labe[i]/inst_count);
		net_entropy = net_entropy + prop * entropy ;
	  } 
	  return net_entropy;
	}
	
	double Ent_labels(List<Instance> instances)
	{
	  int no_labels = this.labels.size();
	  int i=0,j=0;
	  int count_per_label[] = new int[no_labels];
	  for ( i=0; i<instances.size(); i++)
	    count_per_label[instances.get(i).label]++;
	  int inst_count = instances.size();
	  double entropy=0.0,prop;
	  int labe[] = new int[no_labels];
	  for( i=0; i < inst_count; i++)
	  {
		for(j=0; j<no_labels; j++)
		{
		  if( instances.get(i).label == j)
		    labe[j]++;
		}
	  }
	  for(i=0; i< this.labels.size(); i++)
	  {
	    prop = ((double)labe[i]/inst_count);
		if( prop!=0 && prop!=1)
		  entropy = entropy + (-1)*prop * (Math.log(prop)/Math.log(2));
	  } 
	  return entropy;
	}
	

	DecisionTreeImpl(DataSet train, DataSet tune) {

		this.labels = train.labels;
		this.attributes = train.attributes;
		this.attributeValues = train.attributeValues;
		
		int inst_count = train.instances.size();
		int labl_count = this.labels.size();
		int labe[] = new int[labl_count];
		int max =0,i,j=0;
		for( i=0; i < inst_count; i++)
		{
		  for(j=0; j<labl_count; j++)
		  {
		    if( train.instances.get(i).label == j)
			{
			  labe[j]++;
			  if(labe[j] > labe[max])
			     max = j;
			}
		  }
		}
		
		List<Integer> attrbts = new ArrayList<Integer>();
		for(i=0; i < (this.attributes.size()) ; i++)     
		  attrbts.add(i);

		this.root = buildtree(train.instances,attrbts,max,-1);

		double acc = Accuracy(tune.instances);

		double cur_max_accuracy=0.0, prev_max_accuracy=acc;	
		int max_acc_tre_index=0;
		int depth, cur_min_depth;
		
		do
		{
		    depth=0;
			cur_min_depth =99999;
			cur_max_accuracy = 0;
	    	Prunetree( this.root , tune.instances, depth);
	    	for(i=0; i<pr_tr_list.size();i++)
	    	{
			  if(pr_tr_list.get(i).accuracy == cur_max_accuracy)
			  {
			    if(pr_tr_list.get(i).depth < cur_min_depth)
				{
				  cur_max_accuracy = pr_tr_list.get(i).accuracy;
		     	  max_acc_tre_index = i;
				  cur_min_depth = pr_tr_list.get(i).depth ;
				}
			  }
	    	  else if(pr_tr_list.get(i).accuracy >cur_max_accuracy)
		      {
		        cur_max_accuracy = pr_tr_list.get(i).accuracy;
		     	max_acc_tre_index = i;
				cur_min_depth = pr_tr_list.get(i).depth ;
		      }
		    }
			if(cur_max_accuracy>=prev_max_accuracy)
			{
		        root = pr_tr_list.get(max_acc_tre_index).tree_root;
				prev_max_accuracy = cur_max_accuracy;
			}
		  pr_tr_list.clear();
		}
		while(cur_max_accuracy>=acc);
	}
	

	
	
	void Prunetree( DecTreeNode r_node, List<Instance> tn_instances, int depth)
	{ 

	  if(!r_node.terminal)
	  {
       int i=0;
	  List<DecTreeNode> cpy_childs_list = MakeCopyList(r_node.children);	  
	  r_node.children = null;
	  r_node.terminal = true;
	 
	  double accuracy = Accuracy( tn_instances);

	  AccNode abc = new AccNode();
	  abc.tree_root = MakeCopyNode(root);
	  abc.accuracy = accuracy;
	  abc.depth = depth;
	   
	  this.pr_tr_list.add(abc);
	   
	  r_node.terminal = false;
      r_node.children = new ArrayList<DecTreeNode>();
         r_node.children = MakeCopyList(cpy_childs_list);
      depth++;
	  for(i=0; i<(r_node.children.size());i++)
	      Prunetree(r_node.children.get(i),tn_instances,depth);
		 
	  }

	}
	
	DecTreeNode MakeCopyNode( DecTreeNode node)
	{
	  DecTreeNode copy = new DecTreeNode(node.label, node.attribute, node.parentAttributeValue, node.terminal);
	  if(!node.terminal)
		 copy.children = MakeCopyList(node.children);
	  return copy;
	}
	
	List<DecTreeNode> MakeCopyList( List<DecTreeNode> list)
	{
	    int i=0;
	    List<DecTreeNode> copylist = new ArrayList<DecTreeNode>(list.size());
		for(i=0;i<list.size();i++)
		{
		  DecTreeNode child = MakeCopyNode(list.get(i));
		  copylist.add(child);
		}
	  
	  return copylist;
	}
	
	double Accuracy ( List<Instance> instances_set)
	{
	  int i=0, j=0;
	  int correct_count = 0, wrong_count=0;
	  for( i=0; i < instances_set.size() ; i++)
	  {
	    int lbl = this.labels.indexOf(classify(instances_set.get(i)));
		if(lbl == (instances_set.get(i).label))
		  correct_count++;
		else
		  wrong_count++;
	  }
	  double accuracy = ((double)(correct_count))/(instances_set.size());
	  return accuracy;
	}

	
	@Override
	public String classify(Instance instance) {
		
		DecTreeNode node = new DecTreeNode(root.label, root.attribute, root.parentAttributeValue, root.terminal);
		if(!root.terminal)
		   node.children = MakeCopyList(root.children);
		int attr;
		int attrval;
		while(!node.terminal)
		{
		  attr = node.attribute;
		  attrval = instance.attributes.get(attr);
		  node = node.children.get(attrval);
		}
		return this.labels.get(node.label);
	}

	@Override
	/**
	 * Print the decision tree in the specified format
	 */
	public void print() {

		printTreeNode(root, null, 0);
	}
	

	public void printTreeNode(DecTreeNode p, DecTreeNode parent, int k) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < k; i++) {
			sb.append("    ");
		}
		String value;
		if (parent == null) {
			value = "ROOT";
		} else{
			String parentAttribute = attributes.get(parent.attribute);
			value = attributeValues.get(parentAttribute).get(p.parentAttributeValue);
		}
		sb.append(value);
		if (p.terminal) {
			sb.append(" (" + labels.get(p.label) + ")");
			System.out.println(sb.toString());
		} else {
			sb.append(" {" + attributes.get(p.attribute) + "?}");
			System.out.println(sb.toString());
			for(DecTreeNode child: p.children) {
				printTreeNode(child, p, k+1);
			}
		}
	}

	@Override
	public void rootInfoGain(DataSet train) {

		
		this.labels = train.labels;
		this.attributes = train.attributes;
		this.attributeValues = train.attributeValues;
		
		double entropy_labels = Ent_labels(train.instances);    //entropy
		double entropy_given_attribute[] = new double[this.attributes.size()];
	    double info_gain[] = new double[this.attributes.size()];
	    double max_info_gain=0.0;
	    int attr_with_max_info_gain=0;
	    int i;
	    for(i=0 ; i< attributes.size() ; i++)
	    {
	      entropy_given_attribute[i] = Ent_giv_attr(train.instances, i);
	      info_gain[i] = entropy_labels - entropy_given_attribute[i];
		  System.out.print(this.attributes.get(i)+" ");
		  System.out.format("%.5f\n", info_gain[i]);
	    }
		
	}
}
