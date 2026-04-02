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

        HashMap<String, HashSet<String>> neighbors = this.bookGraph.adj.get(book);
        if (neighbors == null || neighbors.isEmpty()) {
            return "NONE";
        }

        // maintain a min-heap of 5 elements
        // break tie by key alphabetically
        Queue<Map.Entry<String, HashSet<String>>> pq =
                new PriorityQueue<>((a, b)-> {
                    int cmp = Integer.compare(a.getValue().size(), b.getValue().size());
                    if (cmp != 0) {
                        return cmp;
                    }
                    return b.getKey().compareTo(a.getKey());
                });

        for (Map.Entry<String, HashSet<String>> entry : neighbors.entrySet()) {
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
            Map.Entry<String, HashSet<String>> entry = pq.poll();
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
            HashMap<String, HashSet<String>> neighbors = this.bookGraph.adj.get(book);
            if (neighbors == null || neighbors.isEmpty()) {
                continue;
            }
            for (String neighbor : neighbors.keySet()) {
                if (books.contains(neighbor)) {
                    continue;
                }
                cumulativeWeight.put(neighbor, cumulativeWeight.getOrDefault(neighbor, 0) + this.bookGraph.adj.get(book).get(neighbor).size());
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
        HashSet<String> likedByTarget = this.userGraph.adj.get(targetUser);
        // maintain a min heap of size 5 to keep the top 5 similar users
        Queue<Map.Entry<String, Double>> pq = new PriorityQueue<>((a, b)->{
            int cmp = Double.compare(a.getValue(), b.getValue());
            if (cmp != 0) {
                return cmp;
            }
            return b.getKey().compareTo(a.getKey());
        });
        HashSet<String> candidateBooks = new HashSet<>();
        // compute the Jaccard similarity between each pair of users
        for (String user : this.userGraph.adj.keySet()) {
            // if is target user, skip
            if (user.equals(targetUser)) {
                continue;
            }
            // get the book list by current user
            HashSet<String> likedByUser = this.userGraph.adj.get(user);

            // add the book list to candidate books
            // not including the ones liked by the target user
            candidateBooks.addAll(likedByUser);
            candidateBooks.removeAll(likedByTarget);

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

        // Get the top 5 taste twins
        ArrayList<String> tasteTwins = new ArrayList<>();
        while (!pq.isEmpty()) {
            Map.Entry<String, Double> entry = pq.poll();
            tasteTwins.add(entry.getKey());
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
            for (String u : this.userGraph.adj.get(book)) {
                if (tasteTwins.contains(u)) {
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
        ArrayList<String> hiddenGem = new ArrayList<>();
        while (!bookPq.isEmpty()) {
            Map.Entry<String, Double> entry = bookPq.poll();
            hiddenGem.add(entry.getKey());
        }
        if (hiddenGem.isEmpty()) {
            return "NONE";
        }
        Collections.reverse(hiddenGem);
        return  String.join(",", hiddenGem);
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
            for (String b1 : books){
                for (String b2 : books){
                    if (!b1.equals(b2)){
                        graph.addEdge(b1, b2, user);
                        graph.addEdge(b2, b1, user);
                    }
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
            HashSet<String> books = userToBooks.get(user);
            // add vertices
            for (String book : books){
                graph.addVertex(user);
                graph.addVertex(book);
            }
            // add undirected edges
            for (String b : books){
                graph.addEdge(b, user);
            }
        }
        return graph;
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
    // Book1 - (Book2 - Users in Common)
    HashMap<String, HashMap<String, HashSet<String>>> adj;
    // Constructor
    public BookGraph(){
        this.adj = new HashMap<>();
    }
    public void addVertex(String book){
        if (!this.adj.containsKey(book)){
            this.adj.put(book, new HashMap<>());
        }
    }
    public void addEdge(String book1, String book2, String user){
        if (book1.equals(book2)){
            return;
        }
        addVertex(book1);
        if (!this.adj.get(book1).containsKey(book2)){
            this.adj.get(book1).put(book2, new HashSet<>());
        }
        this.adj.get(book1).get(book2).add(user);
    }
    public int getEdgeWeight(String book1, String book2){
        return this.adj.get(book1).get(book2).size();
    }
    public HashSet<String> getCommonUsers(String book1, String book2){
        return this.adj.get(book1).get(book2);
    }
    public Set<String> getNeighbors(String book){
        return this.adj.get(book).keySet();
    }
}

/**
 * The UserGraph data structure
 * Key - User
 * Value - Set of books liked by the user
 */
class UserGraph {
    HashMap<String, HashSet<String>> adj;
    public UserGraph(){
        this.adj = new HashMap<>();
    }
    public void addVertex(String v){
        if (!this.adj.containsKey(v)){
            this.adj.put(v, new HashSet<>());
        }
    }
    public void addEdge(String v1, String v2){
        this.adj.get(v1).add(v2);
        this.adj.get(v2).add(v1);
    }
}