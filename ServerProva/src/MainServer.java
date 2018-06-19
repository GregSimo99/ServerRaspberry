import java.net.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


import java.io.*;
import com.pi4j.*;
import com.pi4j.io.*;
import com.pi4j.io.gpio.*;

public class MainServer {

	public static void main(String[] args) throws IOException{
		ServerSocket ss = new ServerSocket();
		Socket s;
		boolean sx=false, dx=false, gas=false, freno=false;
		int x;
		
		ss=new ServerSocket(1999);
		System.out.println("Avviato server sulla porta"+1999+"...");
		final GpioController gpio = GpioFactory.getInstance();
		
		final GpioPinDigitalOutput pin1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01);
	    final GpioPinDigitalOutput pin2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02);
	    final GpioPinDigitalOutput pin3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03);
	    final GpioPinDigitalOutput pin4 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04);
	    
		while (true) {
			s=ss.accept();
			x=s.getInputStream().read();
			//System.out.println("Messaggio: " + x);
			if (x==1 && dx==false) {
				sx=true;
				System.out.println("Sinistra");
				pin1.high();
				//motore sx on
			}
			if (x==2) {
				sx=false;
				pin1.low();
				//motore sx off
			}
			if (x==3 && sx==false) {
				dx=true;
				System.out.println("Destra");
				pin2.high();
				//motore dx on
			}
			if (x==4) {
				dx=false;
				pin2.low();
				//motore dx off
			}
			if (x==5 && freno==false) {
				gas=true;
				System.out.println("Avanti");
				pin3.high();
				//motore gas on
			}
			if (x==6) {
				gas=false;
				pin3.low();
				//motore gas off
			}
			if (x==7 && gas==false) {
				freno=true;
				System.out.println("Indietro");
				pin4.high();
				//motore freno on
			}
			if (x==8) {
				freno=false;
				pin4.low();
				//motore freno off
			}
			if(x==0) {
				try {
					Process p = Runtime.getRuntime().exec("sudo shutdown -h now");
					p.waitFor();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			s.close();
		}
		//ss.close();
	}

}
