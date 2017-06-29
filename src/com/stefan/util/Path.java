package com.stefan.util;

import com.stefan.objects.BlockedRoadStatus;
import com.stefan.objects.Node;
import com.stefan.objects.Road;

import java.util.*;

/**
 * Created by stersteeg on 06/06/2017.
 */
public class Path {

    public static List<Node> getPath(Node start, Node finish, Map<Road, BlockedRoadStatus> blockedRoads) {
        Node s = start;
        Node f = finish;
        if(s instanceof Road) {
            s = ((Road) s).getNearestNode();
        }
        if(f instanceof Road) {
            f = ((Road) f).getNearestNode();
        }
        List<Node> path = new LinkedList<>();
        HashMap<Node, Integer> distances = new HashMap<>();
        HashMap<Node, Node> previous = new HashMap<>();
        List<Node> vertexQueue = new LinkedList<>();

        vertexQueue.add(s);
        distances.put(s, 0);

        while (!vertexQueue.isEmpty()) {
            Node u = vertexQueue.remove(0);
            for (Road e : u.getRoads()) {
                BlockedRoadStatus status = blockedRoads.get(e);
                if(status != null && status.isBlocked()) {
                    continue;
                }
                Node v = e.getDestination(u);
                double distanceThroughU = distances.get(u) + 1;
                if (!distances.containsKey(v) || distanceThroughU < distances.get(v)) {
                    vertexQueue.remove(v);

                    if(distances.containsKey(v)) {
                        distances.replace(v, (int) distanceThroughU);
                    } else {
                        distances.put(v, (int) distanceThroughU);
                    }
                    if(previous.containsKey(v)) {
                        previous.replace(v, u);
                    } else {
                        previous.put(v, u);
                    }
                    vertexQueue.add(v);

                }
            }
        }
        for (Node vertex = f; vertex != null; vertex = previous.get(vertex)) {
            path.add(vertex);
        }
        Collections.reverse(path);
        if(start instanceof Road && !path.contains(start) && path.size() > 1) {
            ((LinkedList) path).addFirst(start);
        }
        if(finish instanceof Road && !path.contains(finish) && (path.size() > 1 || f.equals(s)) ) {
            ((LinkedList) path).addLast(finish);
        }
        return path;
    }
}
