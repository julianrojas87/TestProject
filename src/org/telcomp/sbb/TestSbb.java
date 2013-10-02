package org.telcomp.sbb;

import java.util.HashMap;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.slee.ActivityContextInterface;
import javax.slee.Address;
import javax.slee.RolledBackContext;
import javax.slee.SbbContext;
import javax.slee.nullactivity.NullActivity;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;
import javax.slee.nullactivity.NullActivityFactory;
import javax.slee.serviceactivity.ServiceActivity;
import javax.slee.serviceactivity.ServiceActivityFactory;
import javax.slee.serviceactivity.ServiceStartedEvent;

import org.telcomp.events.EndMediaCallTelcoServiceEvent;
import org.telcomp.events.EndPresenceTelcoServiceEvent;
import org.telcomp.events.EndSendEmailTelcoServiceEvent;
import org.telcomp.events.EndThirdPartyCallTelcoServiceEvent;
import org.telcomp.events.EndWSInvocatorEvent;
import org.telcomp.events.StartMediaCallTelcoServiceEvent;
import org.telcomp.events.StartPresenceTelcoServiceEvent;
import org.telcomp.events.StartSendEmailTelcoServiceEvent;
import org.telcomp.events.StartThirdPartyCallTelcoServiceEvent;
import org.telcomp.events.StartWSInvocatorEvent;

public abstract class TestSbb implements javax.slee.Sbb {
	
	private NullActivityFactory nullActivityFactory;
	private NullActivityContextInterfaceFactory nullACIFactory;
	private ServiceActivityFactory saf;

	public void onServiceStartedEvent(ServiceStartedEvent  event, ActivityContextInterface aci) {
		ServiceActivity sa = saf.getActivity();
		if(sa.equals(aci.getActivity())){
			aci.detach(this.sbbContext.getSbbLocalObject());
			
			
			//HashMap<String, Object> hashMap = new HashMap<String, Object>();
			//hashMap.put("caller", (String) "1061698729");
			//hashMap.put("callee", (String) "1061700875");
			//StartThirdPartyCallTelcoServiceEvent startEvent = new StartThirdPartyCallTelcoServiceEvent(hashMap);
			
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("uriSip", (String) "sip:1061698729@192.175.16.117:5061;line=cf9deb9106071ad");
			//hashMap.put("text", (String) "Hello Julian!. This is a test from Media Call Service");
			hashMap.put("text", (String) "Medellin / Eldorado Colombia SKBO 04-43N 074-09W 2548M  Jun 06 2013 - 01:00 AM EDT " +
					"/ 2013.06.06 0500 UTC   from the N 010 degrees at 3 MPH 3 KT:0   greater than 7 miles:0   partly cloudy   " +
					"46 F 8 C   44 F 7 C   93%   30.35 in. Hg 1027 hPa  Success. This was a message provided by telcomp services. " +
					"Telcomp development team wish you a nice day. Bye bye!");
			//hashMap.put("text", (String) "Medellin / Eldorado Colombia");
			StartMediaCallTelcoServiceEvent startEvent = new StartMediaCallTelcoServiceEvent(hashMap);
			
			//HashMap<String, Object> hashMap = new HashMap<String, Object>();
			//hashMap.put("parameter", (String) "username");
			//hashMap.put("value", (String) "Julian Rojas");
			//StartPresenceTelcoServiceEvent startPresence = new StartPresenceTelcoServiceEvent(hashMap);
			
			ActivityContextInterface nullAci = this.createNullActivityACI();
			nullAci.attach(this.sbbContext.getSbbLocalObject());
			
			//this.fireStartThirdPartyCallTelcoServiceEvent(startEvent, nullAci, null);
			this.fireStartMediaCallTelcoServiceEvent(startEvent, nullAci, null);
			//this.fireStartPresenceTelcoServiceEvent(startPresence, nullAci, null);
			//String wsdl ="http://192.168.190.55:8084/EWSApp-war/TwitterWebService?wsdl";
			//String operationName = "sendTwitterMessage";
			//HashMap<String, String> operationInputs = new HashMap<String, String>();
			//operationInputs.put("text", "message from test client");
			//operationInputs.put("callee", "@julianrm87");
			
			//StartSendEmailTelcoServiceEvent startEm = new StartSendEmailTelcoServiceEvent("julian.rojas87@gmail.com", "Text email.", null);
			//this.fireStartSendEmailTelcoServiceEvent(startEm, nullAci, null);
			//HashMap<String, Object> hashMap = new HashMap<String, Object>();
			//hashMap.put("serviceWSDL", (String) wsdl);
			//hashMap.put("operationName", (String) operationName);
			//hashMap.put("operationInputs", (HashMap<String, String>) operationInputs);
			//hashMap.put("backupFile", (String) "");
			//StartWSInvocatorEvent startWS = new StartWSInvocatorEvent(hashMap);
			//this.fireStartWSInvocatorEvent(startWS, nullAci, null);
		}
	}
	
	public void onEndThirdPartyCallTelcoServiceEvent(EndThirdPartyCallTelcoServiceEvent event, ActivityContextInterface aci){
		System.out.println("End Third Party Call Event Received!!!!!");
		System.out.println("Caller: "+event.getCaller());
		System.out.println("Callee: "+event.getCallee());
		System.out.println("Call Result: "+event.getResult());
		aci.detach(this.sbbContext.getSbbLocalObject());
	}
	
	public void onEndMediaCallTelcoServiceEvent(EndMediaCallTelcoServiceEvent event, ActivityContextInterface aci){
		System.out.println("End Media Call Event Received!!!!!");
		System.out.println("Call commited: "+event.getCommited());
		aci.detach(this.sbbContext.getSbbLocalObject());
	}
	
	public void onEndWSInvocatorEvent(EndWSInvocatorEvent event, ActivityContextInterface aci){
		System.out.println("WS Invocator Response received!!!");
		HashMap<String, List<String>> operationOutputs = event.getOperationOutputs();
		String response = operationOutputs.get("return").get(0);
		System.out.println("Response from Twitter WS: "+response);
		
		aci.detach(this.sbbContext.getSbbLocalObject());
	}
	
	public void onEndSendEmailTelcoServiceEvent(EndSendEmailTelcoServiceEvent event, ActivityContextInterface aci){
		System.out.println("Send Email Response received!!!");
		System.out.println("Response from Send Email Service: "+event.getSended());
		aci.detach(this.sbbContext.getSbbLocalObject());
	}
	
	public void onEndPresenceTelcoServiceEvent(EndPresenceTelcoServiceEvent event, ActivityContextInterface aci){
		System.out.println("Presence Response received!!!");
		System.out.println("Response from Presence Service: "+event.getState());
		aci.detach(this.sbbContext.getSbbLocalObject());
	}
	
	private ActivityContextInterface createNullActivityACI(){
		NullActivity nullActivity = this.nullActivityFactory.createNullActivity();
		ActivityContextInterface nullActivityACI = null;
		nullActivityACI = this.nullACIFactory.getActivityContextInterface(nullActivity);
		return nullActivityACI;
	}
	
	// TODO: Perform further operations if required in these methods.
	public void setSbbContext(SbbContext context) { 
		this.sbbContext = context;
		try {
			Context ctx = (Context) new InitialContext().lookup("java:comp/env");
			nullActivityFactory = (NullActivityFactory) ctx.lookup("slee/nullactivity/factory");
			nullACIFactory = (NullActivityContextInterfaceFactory) ctx.lookup("slee/nullactivity/activitycontextinterfacefactory");
			saf = (ServiceActivityFactory) ctx.lookup("slee/serviceactivity/factory"); 
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
    public void unsetSbbContext() { this.sbbContext = null; }
    
    // TODO: Implement the lifecycle methods if required
    public void sbbCreate() throws javax.slee.CreateException {}
    public void sbbPostCreate() throws javax.slee.CreateException {}
    public void sbbActivate() {}
    public void sbbPassivate() {}
    public void sbbRemove() {}
    public void sbbLoad() {}
    public void sbbStore() {}
    public void sbbExceptionThrown(Exception exception, Object event, ActivityContextInterface activity) {}
    public void sbbRolledBack(RolledBackContext context) {}
    
    public abstract void fireStartThirdPartyCallTelcoServiceEvent(StartThirdPartyCallTelcoServiceEvent event, ActivityContextInterface aci, Address address);
    public abstract void fireStartMediaCallTelcoServiceEvent(StartMediaCallTelcoServiceEvent event, ActivityContextInterface aci, Address address);
    public abstract void fireStartWSInvocatorEvent(StartWSInvocatorEvent event, ActivityContextInterface aci, Address address);
    public abstract void fireStartSendEmailTelcoServiceEvent(StartSendEmailTelcoServiceEvent event, ActivityContextInterface aci, Address address);
    public abstract void fireStartPresenceTelcoServiceEvent(StartPresenceTelcoServiceEvent event, ActivityContextInterface aci, Address address);
	

	
	/**
	 * Convenience method to retrieve the SbbContext object stored in setSbbContext.
	 * 
	 * TODO: If your SBB doesn't require the SbbContext object you may remove this 
	 * method, the sbbContext variable and the variable assignment in setSbbContext().
	 *
	 * @return this SBB's SbbContext object
	 */
	
	protected SbbContext getSbbContext() {
		return sbbContext;
	}

	private SbbContext sbbContext; // This SBB's SbbContext

}
