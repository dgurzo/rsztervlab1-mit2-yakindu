package hu.bme.mit.yakindu.analysis.workhere;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.stext.stext.EventDefinition;
import org.yakindu.sct.model.stext.stext.VariableDefinition;

import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

public class Main {
	@Test
	public void test() {
		main(new String[0]);
	}
	
	public static void main(String[] args) {
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		
		// Reading model
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		
		// Previous state for 2.3
		State previousState = null;
		
		// State number for 2.5
		int cnt = 1;
		
		// Events and variables for rask 4.4
		List<String> variables = new LinkedList<>();
		List<String> events = new LinkedList<>();
		
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			// Task 2
			if(content instanceof State) {
				State state = (State) content;
				// 2.3
				if(previousState != null) {
					//System.out.println(previousState.getName() + " -> " + state.getName());
				}
				else {
					//System.out.println(state.getName());
				}
				previousState = state;
				
				// 2.4
				if(state.getOutgoingTransitions().size() == 0) {
					//System.out.println(state.getName() + " csapda");
				}
				
				// 2.5
				if(state.getName().equals("")) {
					state.setName("State" + cnt);
					cnt++;
					//System.out.println(state.getName());
				}
			}
			
			// 4.3
			if(content instanceof VariableDefinition) {
				VariableDefinition var = (VariableDefinition) content;
				//System.out.println("Variable: " + var.getName());
				variables.add(var.getName());
			}
			
			if(content instanceof EventDefinition) {
				EventDefinition event = (EventDefinition) content;
				//System.out.println("Event: " + event.getName());
				events.add(event.getName());
			}
		}
		
		// Task 4.4
		System.out.println("public static void print(IExampleStatemachine s) {");
		for(int i = 0; i < events.size(); i++) {
			// first letter to uppercase
			String str = events.get(i).substring(0, 1).toUpperCase() + events.get(i).substring(1);
			System.out.println("System.out.println(\"" + events.get(i) + " = \" + s.getSCInterface().get" + str + "());");
		}
		for(int i = 0; i < variables.size(); i++) {
			// first letter to uppercase
			String str = variables.get(i).substring(0, 1).toUpperCase() + variables.get(i).substring(1);
			System.out.println("System.out.println(\"" + variables.get(i).toUpperCase().charAt(0) + " = \" + s.getSCInterface().get" + str + "());");
		}
		System.out.println("}\n\n");
		
		// Task 4.5
		System.out.println("public static void main(String[] args) throws IOException {\n" + 
				"\tExampleStatemachine s = new ExampleStatemachine();\n" + 
				"\ts.setTimer(new TimerService());\n" +
				"\tRuntimeService.getInstance().registerStatemachine(s, 200);\n");
		System.out.println("\ts.init();\n" + 
				"\ts.enter();\n" + 
				"\ts.runCycle();\n");
		System.out.println("\tBufferedReader br = new BufferedReader(new InputStreamReader(System.in));\n" + 
				"\twhile(true) {\n" + 
				"\t\tString command = br.readLine();\n" +
				"\t\tif(command.equals(\"exit\")) {\n" +
				"\t\t\tSystem.out.println(\"Exit now\");\n" +
				"\t\t\tSystem.exit(0);\n" +
				"\t\t}");
		for(int i = 0; i < events.size(); i++) {
			String str = events.get(i).substring(0, 1).toUpperCase() + events.get(i).substring(1);
			System.out.println("\t\telse if(command.equals(\"" + events.get(i) + "\")) {\n" +
					"\t\t\ts.raise" + str + "();\n" +
					"\t\t\ts.runCycle();\n" +
					"\t\t}");
		}
		System.out.println("\t\telse {\n" +
				"\t\t\tSystem.out.println(\"Bad command!\");\n" +
				"\t\t\tbreak;\n" +
				"\t\t}\n" +
				"\t\tprint(s);");
		System.out.println("\t}");
		System.out.println("\tbr.close();\n" +
				"\tSystem.exit(0);\n" +
				"}\n");
		
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}
