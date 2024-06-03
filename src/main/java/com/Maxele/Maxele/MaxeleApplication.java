package com.Maxele.Maxele;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import org.slf4j.Logger;

@SpringBootApplication
public class MaxeleApplication {
	private static int contatrovate = 0;
	private static double maxaltitudine = Double.MIN_VALUE;
	private static List<String> bestpath = new ArrayList<>();
	private static Graph graph;
	private static final Logger logger = LoggerFactory.getLogger(MaxeleApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MaxeleApplication.class, args);
		System.out.println("prova");
		Gson gson = new Gson();

		try (FileReader reader = new FileReader("graph.json")) {
			Type graphType = new TypeToken<Map<String, Node>>(){}.getType();
			Map<String, Node> nodes = gson.fromJson(reader, graphType);

			graph = new Graph();
			graph.setNodes(nodes);
			/*for (Map.Entry<String, Node> entry : graph.getNodes().entrySet()) {
				String nodeId = entry.getKey();
				Node node = entry.getValue();
			}*/


			List<String> bestPath = findMaximumPaths("462648177", "462646721");

			logger.info("Best path with maximum elevation gain: " + bestPath);
			logger.info("Maximum elevation gain: " + maxaltitudine + " meters");
			System.out.println("Best path with maximum elevation gain: " + bestPath);
			System.out.println("Maximum elevation gain: " + maxaltitudine + " meters");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static List<String> findMaximumPaths(String startNode, String endNode) {
		Set<String> visited = new HashSet<>();
		findPathsRecursive(startNode, endNode, new ArrayList<>(Collections.singletonList(startNode)), visited);
		return bestpath;
	}

	private static void findPathsRecursive(String currentNode, String endNode, List<String> currentPath, Set<String> visited) {
		if (currentNode.equals(endNode)) {
			contatrovate++;
			if (contatrovate % 10000 == 0) {
				logger.info("Paths found: " + contatrovate);
				System.out.println("Paths found: " + contatrovate);
			}
			List<Double> tempele = getelevazioneroute(currentPath);
			double tempeletotale = calculateElevationGain(tempele);
			if (tempeletotale > maxaltitudine) {
				maxaltitudine = tempeletotale;
				bestpath = new ArrayList<>(currentPath);
				logger.info("New best elevation gain: " + maxaltitudine + " meters");
				System.out.println("New best elevation gain: " + maxaltitudine + " meters");
			}
			return;
		}

		visited.add(currentNode);

		for (String neighbor : graph.getNodes().get(currentNode).getEdges()) {
			if (!visited.contains(neighbor)) {
				currentPath.add(neighbor);
				findPathsRecursive(neighbor, endNode, currentPath, visited);
				currentPath.remove(currentPath.size() - 1);
			}
		}

		visited.remove(currentNode);
	}

	private static double calculateElevationGain(List<Double> path) {
		double elevationGain = 0;

		for (int i = 1; i < path.size(); i++) {
			double elevationDifference = path.get(i) - path.get(i - 1);
			if (elevationDifference > 0) {
				elevationGain += elevationDifference;
			}
		}

		return elevationGain;
	}

	private static List<Double> getelevazioneroute(List<String> path) {
		List<Double> elevationetot = new ArrayList<>();

		for (String key : path) {
			Node node = graph.getNodes().get(key);
			double elevation = node.getEle();
			elevationetot.add(elevation);
		}

		return elevationetot;
	}

}
