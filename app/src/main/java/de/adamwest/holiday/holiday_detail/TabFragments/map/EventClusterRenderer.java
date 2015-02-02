package de.adamwest.holiday.holiday_detail.TabFragments.map;

import android.content.Context;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import de.adamwest.R;
import de.adamwest.helper.Constants;

/**
 * Created by philip on 14/01/15.
 */
public class EventClusterRenderer extends DefaultClusterRenderer<EventClusterItem> {

    public EventClusterRenderer(Context context, GoogleMap map, ClusterManager<EventClusterItem> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster cluster) {
        // Always render clusters.
        return cluster.getSize() > 1;
    }
    //init noncluster object
    @Override
    protected void onBeforeClusterItemRendered(EventClusterItem cluster, MarkerOptions markerOptions) {
        int imageId = R.drawable.map_marker_picture;

        //TODO CUSTOM MARKER FOR EACH EVENT TYPE
        if(cluster.getEvent().getType().equals(Constants.TYPE_IMAGE)) {
            imageId = R.drawable.map_marker_picture;
        }
        else if(cluster.getEvent().getType().equals(Constants.TYPE_VIDEO)) {
            imageId = R.drawable.map_marker_video;
        }
        else if(cluster.getEvent().getType().equals(Constants.TYPE_TEXT)) {
            imageId = R.drawable.map_marker_text;
        }
        markerOptions.icon(BitmapDescriptorFactory.fromResource(imageId));

    }
}
