package hu.bme.mit.yakindu.analysis.workhere;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;



public class RunStatechart {
	
	public static void main(String[] args) throws IOException {
		ExampleStatemachine s = new ExampleStatemachine();
		s.setTimer(new TimerService());
		RuntimeService.getInstance().registerStatemachine(s, 200);
		s.init();
		s.enter();
		s.runCycle();
		
		/*print(s);
		s.raiseStart();
		s.runCycle();
		System.in.read();
		s.raiseWhite();
		s.runCycle();
		print(s);
		System.exit(0);*/
		
		// 3.5
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while(true) {
			String command = br.readLine();
			if(command.equals("exit")) {
				System.out.println("Exit now");
				System.exit(0);
			}
			else if(command.equals("start")) {
				s.raiseStart();
				s.runCycle();
			}
			else if(command.equals("white")) {
				s.raiseWhite();
				s.runCycle();
			}
			else if(command.equals("black")) {
				s.raiseBlack();
				s.runCycle();
			}
			else {
				System.out.println("Bad command!");
				break;
			}
			// WhiteTime and BlackTime after every command
			print(s);
		}
		br.close();
		System.exit(0);
	}

	public static void print(IExampleStatemachine s) {
		System.out.println("W = " + s.getSCInterface().getWhiteTime());
		System.out.println("B = " + s.getSCInterface().getBlackTime());
	}
}
