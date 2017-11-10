//package ad2.ss16.pa;

import java.util.*;

/**
 * Klasse zum Berechnen eines k-MST mittels Branch-and-Bound. Hier sollen Sie
 * Ihre Loesung implementieren.
 */

public class KMST extends AbstractKMST { // das gewicht von kanten minimale zu finden.Die menge von Kanten aber k -1
    private int numNodes;
    private HashSet<Edge> edges;
    private Edge[] edgeList;
    private int k;

    /**
     * Der Konstruktor. Hier ist die richtige Stelle fuer die
     * Initialisierung Ihrer Datenstrukturen.
     *
     * @param numNodes
     *            Die Anzahl der Knoten
     * @param numEdges
     *            Die Anzahl der Kanten
     * @param edges
     *            Die Menge der Kanten
     * @param k
     *            Die Anzahl der Knoten, die Ihr MST haben soll
     */
    public KMST(Integer numNodes, Integer numEdges, HashSet<Edge> edges, int k) {
        this.numNodes = numNodes;
        this.edges = edges;
        this.k = k;

        this.edgeList = new Edge[this.edges.size()];

        int count = 0;

        Iterator<Edge> edge = edges.iterator();
        while(edge.hasNext()){
            Edge e = edge.next();
            edgeList[count++] = e;
        }

        Arrays.sort(edgeList); // nach Gewicht wird es sortiert.
    }

    /**
     * Diese Methode bekommt vom Framework maximal 30 Sekunden Zeit zur
     * Verfuegung gestellt um einen gueltigen k-MST zu finden.
     *
     * <p>
     * F&uuml;gen Sie hier Ihre Implementierung des Branch-and-Bound Algorithmus
     * ein.
     * </p>
     */
    @Override
    public void run() {

        Edge[] sortedUp = edgeList;

        for(Edge e : sortedUp) {
            HashSet<Edge> solution = new HashSet<Edge>();
            solution.add(e);
            HashSet<Edge> solUp = new HashSet<Edge>();
            solUp.add(e);
            int up = upperBound(2, solUp); // Prim algoritm // 2 ist eine Kante 2 Knoten deswegen
            setSolution(up, solUp); // Falls up kleiner als die globale obere Schranke ist, dann ist up neue
            // Obere Schranke. 	// int newUpperBound, Set<Edge> newSolution
            HashMap<Integer, Boolean> visit = new HashMap<>();
            visit.put(e.node1, true); // Alle Knoten bis jetzt besucht haben.
            visit.put(e.node2, true);
            branchAndBound(solution, 2, visit);
        }
    }

    private void branchAndBound(HashSet<Edge> solution, int pos, HashMap<Integer, Boolean> visit){

        int low = lowerBound(solution, pos);

        if(this.getSolution().getUpperBound() <= low){ // Alle weitere Lösungen sind schlechter als voriges .
            return;
        }

        for(Edge e : edgeList){
            if(visit.get(e.node1) != null ^ visit.get(e.node2) != null){
                solution.add(e);
                HashSet<Edge> solUp = new HashSet<Edge>();
                solUp.addAll(solution);
                int up = upperBound(pos + 1, solUp);
                // int newUpperBound, Set<Edge> newSolution
                setSolution(up, solUp);
                int ka = (visit.get(e.node1) != null ? e.node2 : e.node1);
                visit.put(ka, true);
                branchAndBound(solution, pos + 1, visit);
                visit.remove(ka);
                solution.remove(e);
            }
        }
    }

    private int upperBound(int pos, HashSet<Edge> solUp){ // solUp nur eine Kante
        int counter = pos;
        int weight = 0;

        HashMap<Integer, Boolean> visit = new HashMap<>();

        for(Edge e : solUp){
            weight = weight + e.weight;
            visit.put(e.node1, true);
            visit.put(e.node2, true);
        }

        while(counter < this.k){  // Prim algorithm
            for(Edge e : edgeList){
                if(visit.get(e.node1) != null ^ visit.get(e.node2) != null){
                    counter++;
                    solUp.add(e);
                    visit.put(e.node1, true);
                    visit.put(e.node2, true);
                    weight = weight + e.weight;
                    break;
                }
            }
        }
        return weight;
    }

    private int lowerBound(HashSet<Edge> solution, int pos){
        int weight = 0;

        HashMap<Integer, Boolean> visit = new HashMap<>();

        for (Edge e : solution){
            weight = weight + e.weight;
            visit.put(e.node1, true);
            visit.put(e.node2, true);
        }

        int count = pos;

        while(count < this.k){     // Kruskal Algoritmasi
            for(Edge e : edgeList){
                if(!(visit.get(e.node1) != null && visit.get(e.node2) != null)){  // birisi true ve ya ikisi false ; ohne zyklus
                    count++;
                    weight = weight + e.weight;
                    visit.put(e.node1, true);
                    visit.put(e.node2, true);
                    break;
                }
            }
        }
        return weight;
    }
}
