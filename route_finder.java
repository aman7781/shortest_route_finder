import java.util.*;

class Route {
    String destination;
    String medium;
    double price;
    double time;

    public Route(String medium, String destination, double price, double time) {
        this.destination = destination;
        this.price = price;
        this.time = time;
        this.medium = medium;
    }
}

class Graph {
    private Map<String, List<Route>> allRoutes = new HashMap<>();
    private List<String> alldestinations = new ArrayList<>();

    public void addRoute(String source, String destination, String medium, double price, double time) {
        Route route = new Route(medium, destination, price, time);
        allRoutes.putIfAbsent(source, new ArrayList<>());
        allRoutes.get(source).add(route);
        alldestinations.add(destination);
    }

    public List<String> findShortestPath(String source, String destination, String searchMode) {
        if (!allRoutes.containsKey(source) || !alldestinations.contains(destination)) {
            return Collections.emptyList();
        }

        Map<String, Double> weights = new HashMap<>();
        Map<String, List<String>> prev = new HashMap<>();
        for (String city : allRoutes.keySet()) {
            weights.put(city, Double.MAX_VALUE);
        }
        for(String city : alldestinations){
            weights.put(city, Double.MAX_VALUE);
        }
        weights.put(source, 0.0);

        PriorityQueue<String> pq = new PriorityQueue<>((a,b) -> {
            if(weights.get(a)==weights.get(b)){
                return 0;
            }
            if(weights.get(a)>weights.get(b)){
                return 1;
            }
            else{
                return -1;
            }
        });
        pq.add(source);

        while (!pq.isEmpty()) {
            String city = pq.poll();
            if (city.equals(destination)) {
                break;
            }

            for (Route r : allRoutes.getOrDefault(city, Collections.emptyList())) {
                double currWeight = weights.get(city);
                double newWeight = currWeight + (searchMode.equals("price") ? r.price : (searchMode.equals("time") ? r.time : r.time + r.price));

                if (weights.get(r.destination) > newWeight) {
                    
                    weights.put(r.destination, newWeight);
                    pq.add(r.destination);
                    prev.put(r.destination, Arrays.asList(city, r.medium));
                }
            }
        }

        List<String> ans = new ArrayList<>();
        String currCity = destination;
        if (!prev.containsKey(currCity)) {
            return Collections.emptyList();
        }
        ans.add(destination);
        while (true) {
            if(prev.get(currCity) == null){
                break;
            }
            List<String> routeInfo = prev.get(currCity);
            ans.add(routeInfo.get(0) + "(" + routeInfo.get(1) + ")");
            currCity = routeInfo.get(0);
        }
        Collections.reverse(ans);
        return ans;
    }
}

public class Main {
    public static void main(String[] args) {
        Graph routes = new Graph();
        routes.addRoute("Delhi", "Mumbai", "flight", 100.0, 60);
        routes.addRoute("Delhi", "Lucknow", "flight", 60.0, 60);
        routes.addRoute("Lucknow", "Mumbai", "flight", 20.0, 60);
        routes.addRoute("Lucknow", "Mumbai", "train", 10.0, 200);
        routes.addRoute("Delhi", "Mumbai", "train", 40.0, 200);
        List<String> ans = routes.findShortestPath("Delhi", "Mumbai", "price");
        if(ans.isEmpty()){
            System.out.println("No Routes Present between source and destination");
        }
        else{
            for(String city: ans){
                System.out.print(city + "->");
            }
        }
    }
}
