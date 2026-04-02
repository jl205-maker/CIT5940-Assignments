import java.util.*;
import java.io.*; // read from file
public class BookRecommender {
    BookGraph bookGraph;
    UserGraph userGraph;
    public BookRecommender(String filename) {
        this.bookGraph = GraphBuilder.buildBookGraph(filename);
        this.userGraph = GraphBuilder.buildUserGraph(filename);
    }
    public String single_book_mn(String book) {
        ArrayList<String> res = new ArrayList<>();

        HashMap<String, BBEdge> neighbors = this.bookGraph.adj.get(book);
        if (neighbors == null || neighbors.isEmpty()) {
            return "NONE";
        }

        // maintain a min-heap of 5 elements
        // break tie by key alphabetically
        Queue<Map.Entry<String, BBEdge>> pq =
                new PriorityQueue<>((a, b)-> {
                    int cmp = Integer.compare(a.getValue().getWeight(), b.getValue().getWeight());
                    if (cmp != 0) {
                        return cmp;
                    }
                    return b.getKey().compareTo(a.getKey());
                });

        for (Map.Entry<String, BBEdge> entry : neighbors.entrySet()) {
            // add a new pair sorted in ascending order
            pq.offer(entry);
            while (pq.size() > 5) {
                pq.poll();
            }
        }
        if (pq.isEmpty()) {
            return "NONE";
        }
        while (!pq.isEmpty()) {
            Map.Entry<String, BBEdge> entry = pq.poll();
            res.add(entry.getKey());
        }
        Collections.reverse(res);
        return String.join(",", res);
    }
    public String like_history_mn(HashSet<String> books) {
        // take every book in the input (like history)
        // look at all its neighbors
        // for each neighbor, accumulate a total score across all input books
        // exclude any books already in the input history
        // return top 5
        ArrayList<String> res = new ArrayList<>();

        HashMap<String, Integer> cumulativeWeight = new HashMap<>();
        for (String book : books) {
            HashMap<String, BBEdge> neighbors = this.bookGraph.adj.get(book);
            if (neighbors == null || neighbors.isEmpty()) {
                continue;
            }
            for (String neighbor : neighbors.keySet()) {
                if (books.contains(neighbor)) {
                    continue;
                }
                cumulativeWeight.put(neighbor, cumulativeWeight.getOrDefault(neighbor, 0) + this.bookGraph.adj.get(book).get(neighbor).getWeight());
            }
        }
        // maintain a min-heap of 5 elements
        // break tie by key alphabetically
        Queue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b)-> {
                    int cmp = Integer.compare(a.getValue(), b.getValue());
                    if (cmp != 0) {
                        return cmp;
                    }
                    return b.getKey().compareTo(a.getKey());
                });
        for (Map.Entry<String, Integer> entry : cumulativeWeight.entrySet()) {
            // add a new pair sorted in ascending order
            pq.offer(entry);
            while (pq.size() > 5) {
                pq.poll();
            }
        }

        if (pq.isEmpty()) {
            return "NONE";
        }

        while (!pq.isEmpty()) {
            Map.Entry<String, Integer> entry = pq.poll();
            res.add(entry.getKey());
        }

        Collections.reverse(res);
        return String.join(",", res);
    }
    public String user_cf(String targetUser){
        // get the book list by target user
        HashSet<String> likedByTarget = this.userGraph.userToBooks.get(targetUser);
        if (likedByTarget == null || likedByTarget.isEmpty()){
            return "NONE";
        }

        // maintain a min heap of size 5 to keep the top 5 similar users
        Queue<Map.Entry<String, Double>> pq = new PriorityQueue<>((a, b)->{
            int cmp = Double.compare(a.getValue(), b.getValue());
            if (cmp != 0) {
                return cmp;
            }
            return b.getKey().compareTo(a.getKey());
        });

        // compute the Jaccard similarity between each pair of users
        for (String user : this.userGraph.userToBooks.keySet()) {
            // if is target user, skip
            if (user.equals(targetUser)) {
                continue;
            }
            // get the book list by current user
            HashSet<String> likedByUser = this.userGraph.userToBooks.get(user);

            // get the set of books liked by both target and user
            Set<String> intersection = new HashSet<>(likedByTarget);
            intersection.retainAll(likedByUser);
            if  (intersection.isEmpty()) {
                continue;
            }

            Set<String> union = new HashSet<>(likedByTarget);
            union.addAll(likedByUser);

            // compute the Jaccard similarity between the current user and the target user
            double js = intersection.size() * (1.0) / union.size();
            pq.offer(new AbstractMap.SimpleEntry<>(user, js));
            while (pq.size() > 5) {
                pq.poll();
            }
        }

        // store the top 5 taste twins
        HashSet<String> candidateUsers = new HashSet<>();
        while (!pq.isEmpty()) {
            Map.Entry<String, Double> entry = pq.poll();
            candidateUsers.add(entry.getKey());
        }
        // compute candidate books
        HashSet<String> candidateBooks = new HashSet<>();
        for(String user : candidateUsers) {
            candidateBooks.addAll(this.userGraph.userToBooks.get(user));
        }
        candidateBooks.removeAll(likedByTarget);
        if (candidateBooks.isEmpty()) {
            return "NONE";
        }

        // maintain a min heap of size 5 to keep the top 5 similar users
        Queue<Map.Entry<String, Double>> bookPq = new PriorityQueue<>((a, b)->{
            int cmp = Double.compare(a.getValue(), b.getValue());
            if (cmp != 0) {
                return cmp;
            }
            return b.getKey().compareTo(a.getKey());
        });

        for (String book : candidateBooks) {
            int denominator = 0;
            int numerator = 0;
            HashSet<String> users = this.userGraph.bookToUsers.get(book);
            if (users == null || users.isEmpty()) {
                continue;
            }
            for (String u : users) {
                if (candidateUsers.contains(u)) {
                    numerator++;
                }
                denominator++;
            }
            double score = (1.0) * numerator / denominator;
            bookPq.offer(new AbstractMap.SimpleEntry<>(book, score));
            while (bookPq.size() > 5) {
                bookPq.poll();
            }
        }
        // Get the top 5 taste twins
        ArrayList<String> result = new ArrayList<>();
        while (!bookPq.isEmpty()) {
            Map.Entry<String, Double> entry = bookPq.poll();
            result.add(entry.getKey());
        }
        if (result.isEmpty()) {
            return "NONE";
        }
        Collections.reverse(result);
        return  String.join(",", result);
    }
    public String shortest_path(String source_bid, String target_bid) {
        // validate inputs
        if (source_bid.equals(target_bid)) {
            return source_bid;
        }
        BookGraph graph = GraphBuilder.FilteredBookGraph(this.bookGraph);

        // validate inputs
        if (!graph.adj.containsKey(source_bid) || !graph.adj.containsKey(target_bid)) {
            return "NONE";
        }

        HashSet<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        // parent map for path reconstruction
        HashMap<String, String> parents = new HashMap<>();
        parents.put(source_bid, null);

        // single source bfs
        queue.offer(source_bid);
        visited.add(source_bid);
        while (!queue.isEmpty()) {
            String curr = queue.poll();

            if (curr.equals(target_bid)) {
                break;
            }

            List<String> neighbors = new ArrayList<>(graph.adj.get(curr).keySet());
            Collections.sort(neighbors);

            for (String neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    parents.put(neighbor, curr);
                    queue.offer(neighbor);
                    visited.add(neighbor);
                }
            }
        }

        // reconstruct path
        // if target is not visited,
        // there does not exist a valid path between src and target
        if (!visited.contains(target_bid)) {
            return "NONE";
        }
        // otherwise, a valid path exists
        Deque<String> path = new ArrayDeque<>();
        String start = target_bid;
        while (start != null) {
            path.push(start);
            start = parents.get(start);
        }

        return String.join("->", path);
    }
}

/**
 * Build specific graph from the input file
 */
class GraphBuilder {
    public static BookGraph buildBookGraph(String filename){
        // Initialize a BookGraph object implemented as a HashMap
        BookGraph graph = new BookGraph();
        // Parse file, get User - Book hashmap
        HashMap<String, HashSet<String>> userToBooks = parseFile(filename);
        // For each user, connect all books they read
        for (String user : userToBooks.keySet()){
            HashSet<String> books = userToBooks.get(user);

            for (String book : books){
                graph.addVertex(book);
            }

            List<String> list = new ArrayList<>(books);
            for (int i = 0; i < list.size(); i++){
                for (int j = i + 1; j < list.size(); j++){
                    graph.addEdge(list.get(i), list.get(j), user);
                    graph.addEdge(list.get(j), list.get(i), user);
                }
            }
        }
        return graph;
    }
    public static UserGraph buildUserGraph(String filename){
        UserGraph graph = new UserGraph();
        // Parse file, get User - Book hashmap
        HashMap<String, HashSet<String>> userToBooks = parseFile(filename);

        for (String user : userToBooks.keySet()){
            graph.addUserVertex(user);
        }
        for (String user : userToBooks.keySet()){
            HashSet<String> books = userToBooks.get(user);
            for (String book : books){
                // add book vertices
                graph.addBookVertex(book);
                // add book <-> user edges
                graph.addEdge(user, book);
            }
        }
        return graph;
    }
    public static BookGraph FilteredBookGraph(BookGraph bg){
        double median = bg.medianEdgeWeight();
        BookGraph filteredGraph = new BookGraph();
        for (String book : bg.adj.keySet()){
            filteredGraph.addVertex(book);
        }
        for (String book : bg.adj.keySet()){

            HashMap<String, BBEdge> neighbors = bg.adj.get(book);
            for (String neighbor : neighbors.keySet()){
                BBEdge edge = neighbors.get(neighbor);

                if (edge.src.compareTo(edge.dest) < 0 && edge.getWeight() >= median){
                    for (String user : edge.users){
                        filteredGraph.addEdge(edge.src, edge.dest, user);
                        filteredGraph.addEdge(edge.dest, edge.src, user);
                    }
                }
            }
        }
        return filteredGraph;
    }
    private static HashMap<String, HashSet<String>> parseFile(String filename){
        HashMap<String, HashSet<String>> userToBooks = new HashMap<>();
        try (BufferedReader bf = new BufferedReader((new FileReader(filename)))) {
            String line;
            while ((line = bf.readLine()) != null) {
                String[] tokens = line.split(",");
                if (!userToBooks.containsKey(tokens[0])) {
                    userToBooks.put(tokens[0], new HashSet<>());
                }
                userToBooks.get(tokens[0]).add(tokens[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userToBooks;
    }

}

/**
 * The BookGraph data structure
 * Stores and manages the graph
 * Represented as an adjacency list
 * Implemented as a HashMap
 * Book - Set of connected books
*/
class BookGraph {
    // Book1 - (Book2 - Edge)
    HashMap<String, HashMap<String, BBEdge>> adj;
    // Constructor
    public BookGraph(){
        this.adj = new HashMap<>();
    }
    // Methods
    public void addVertex(String book){
        if (!this.adj.containsKey(book)){
            this.adj.put(book, new HashMap<>());
        }
    }
    public void addEdge(String book1, String book2, String user){
        if (book1.equals(book2)) return;

        addVertex(book1);

        adj.putIfAbsent(book1, new HashMap<>());
        adj.get(book1).putIfAbsent(book2, new BBEdge(book1, book2));

        adj.get(book1).get(book2).users.add(user);
    }
    public double medianEdgeWeight(){
        // collect all edges
        ArrayList<Integer> weights = new ArrayList<>();
        for (String src : this.adj.keySet()){
            for (BBEdge edge : this.adj.get(src).values()) {
                if (edge.src.compareTo(edge.dest) < 0) {
                    weights.add(edge.getWeight());
                }
            }
        }
        // compute the median edge weight of the given bg
        Collections.sort(weights);
        int n =  weights.size();
        if (n == 0) {
            return 0;
        }
        double median;
        if (n % 2 == 1) {
            median = weights.get(n/2);
        } else {
            median = (weights.get(n/2) + weights.get(n/2-1)) / 2.0;
        }
        return median;
    }
}
class BBEdge {
    String src;
    String dest;
    HashSet<String> users;

    public BBEdge(String src, String dest){
        this.src = src;
        this.dest = dest;
        this.users = new HashSet<>();
    }
    public int getWeight(){
        return this.users.size();
    }
}

/**
 * The UserGraph data structure
 * Key - User
 * Value - Set of books liked by the user
 */
class UserGraph {
    HashMap<String, HashSet<String>> userToBooks;
    HashMap<String, HashSet<String>> bookToUsers;
    public UserGraph(){
        this.userToBooks = new HashMap<>();
        this.bookToUsers = new HashMap<>();
    }
    public void addUserVertex(String u){
        if (!this.userToBooks.containsKey(u)){
            this.userToBooks.put(u, new HashSet<>());
        }
    }
    public void addBookVertex(String b){
        if (!this.bookToUsers.containsKey(b)){
            this.bookToUsers.put(b, new HashSet<>());
        }
    }

    public void addEdge(String u, String b){
        this.userToBooks.get(u).add(b);
        this.bookToUsers.get(b).add(u);
    }
}