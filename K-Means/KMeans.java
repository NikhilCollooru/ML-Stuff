/**
 * A k-means clustering algorithm implementation.
 */
import java.lang.Math;
import java.util.*;

public class KMeans {
	public KMeansResult cluster(double[][] centroids, double[][] instances, double threshold) {

		int no_clusters = centroids.length;
		int no_instances = instances.length;
		int no_features = instances[0].length;
		int cluster_assgn[] = new int[no_instances];
		double new_centroids[][] = new double[no_clusters][no_instances];

		ArrayList<Double> distortions = new ArrayList<Double>();
		double current_distortion,prev_distort;
		
		int i,j,k,iterations=0;
		double temp;

		for(temp=100.0; temp > threshold ; iterations ++)
		{
	    cluster_assgn = DoClusterAssignment(centroids, instances);
		centroids = ComputeNewCentroids(no_clusters, cluster_assgn, instances);
		current_distortion = ComputeDistortion(centroids, instances, cluster_assgn);
		distortions.add(current_distortion);
		
		 if(iterations >0 )
		 {
		   prev_distort = distortions.get(iterations-1);
		   temp = (prev_distort - current_distortion)/(prev_distort);
		 }

		}
		
		KMeansResult res = new KMeansResult();
		res.centroids = new double[no_clusters][no_features];
		res.clusterAssignment = new int[no_instances];
		
		res.centroids = centroids;
		res.clusterAssignment = cluster_assgn;
 
		double[] dis = new double[distortions.size()];
        for(i=0; i<dis.length; i++)
           dis[i] = distortions.get(i);		
		res.distortionIterations = dis;
		
		return res;
	}
	
	
	public int[] DoClusterAssignment( double centroids[][], double[][] instances)
	{
	    int no_features = instances[0].length;
		int no_instances = instances.length;
		int no_clusters = centroids.length;
		int cluster_assgn[] = new int[no_instances];
		int i,j,k,max_dis_inst_no;
		boolean orphaned_centroid =false;
		double max_dist =0;
		int[] clu= new int[no_clusters];
		
		
	do
	{ 
	   orphaned_centroid = false;
	   max_dist =0;
	   max_dis_inst_no=909099;
		for (i=0; i< no_instances; i++)
		{
		  double least_dist =0;
		  for ( j=0; j<no_clusters ; j++)
		  {
		   double dist=0;
		    for (k=0 ; k < no_features ; k++)
			{
			  dist = dist + Math.pow( (instances[i][k] - centroids[j][k]), 2);
			  if(dist >99999999)
			    System.out.println("dist reached infn at instance: " +i);
			}
			
			if(j==0)
			{
			  least_dist = dist;
			  cluster_assgn[i] = 0;
			  if(i==0)
		      {
			    max_dist = dist;
			    max_dis_inst_no = 0;
			  }
			}
			else if (dist <= least_dist)
			{
			  if(dist == least_dist)
			  {
			    if(j< cluster_assgn[i])
				  cluster_assgn[i] = j;
			  }
			  else
			  {
			    least_dist = dist;
			    cluster_assgn[i] =j;
			  }
			} 
          }	
		
	      if(least_dist > max_dist)
		  {
		    max_dist = least_dist;
		    max_dis_inst_no = i;
	      }
			  
		}
		
		  //Increment the cluster count
	      for (i=0 ; i<no_instances ; i++)
		      clu[cluster_assgn[i]]++;
		
		  //Finding orphaned centroid if any
		  this_loop:
		  for (i=0 ; i<no_clusters ; i++)
		  {
		    if (clu[i]==0)
		    { 
			  orphaned_centroid = true;
			  for(j=0; j<no_features;j++)
		         centroids[i][j] = instances[max_dis_inst_no][j];
			  break this_loop;	 
		    }
		  }
	}while(orphaned_centroid);
	
	return cluster_assgn;
    }
	
	public double[][] ComputeNewCentroids(int no_clusters, int cluster_assgn[], double instances[][])
    {	
	    int no_features = instances[0].length;
		int no_instances = instances.length;
		int cluster_count[] = new int[no_clusters];
		
		int i,j;
		//Finding the new centroids
		double[][] n_centr = new double[no_clusters][no_features];
		for(i=0 ; i<no_instances; i++)
		{
		  for(j=0 ; j<no_features; j++)
		    n_centr[cluster_assgn[i]][j] +=instances[i][j]; 
		  cluster_count[cluster_assgn[i]]++;
		}
		
		//Taking average of all the instances assigned to a given cluster
		for(i=0; i<no_clusters; i++)
		  for(j=0 ; j<no_features; j++)
		     n_centr[i][j] = (n_centr[i][j])/cluster_count[i];
	    
		return n_centr;
	}
	
	public double ComputeDistortion(double[][] new_centroids, double[][] instances, int cluster_assgn[])
	{
		double dist =0;
		int i,j,k;
		int no_features = instances[0].length;
		int no_instances = instances.length;
		int no_clusters = new_centroids.length;
		
	    for (i=0; i< no_instances; i++)
		{
		  j = cluster_assgn[i];
		  for (k=0 ; k < no_features ; k++)
			dist = dist + Math.pow( (instances[i][k] - new_centroids[j][k]), 2); 	
		}  
	  return dist;	
	}
	
	
}
