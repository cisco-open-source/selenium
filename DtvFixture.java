package fittest;

import java.io.IOException;
import java.util.Date;

import javax.media.Duration;
import javax.media.Player;
import javax.media.Time;
import javax.tv.locator.InvalidLocatorException;
import javax.tv.locator.Locator;
import javax.tv.locator.LocatorFactory;
import javax.tv.locator.MalformedLocatorException;
import javax.tv.service.SIManager;
import javax.tv.service.Service;
import javax.tv.service.ServiceNumber;
import javax.tv.service.navigation.ServiceList;
import javax.tv.service.selection.ServiceContentHandler;
import javax.tv.service.selection.ServiceContext;

import mhwa.locator.LocatorManager;
import mhwa.locator.dvr.RecordedLocator;
import mhwa.pvr.CannotProcessException;
import mhwa.pvr.Record;
import mhwa.pvr.timeshifting.TimeShiftingUtilities;
import mhwa.selection.DefaultServiceContext;
import mhwa.selection.Utils;
import mhwa.sitk.ServiceNameFilter;

/**
 * <pre>
 * Enable to test:
 * <li>select S1
 * <li>select S2
 * <li>startTimeShiftRecording 
 * <li>wait 20s
 * <li>startPlayTimeShiftAtEnd 
 * <li>setTimeShiftRate -1 
 * <li>wait 10s
 * <li>setTimeShiftRate 0 
 * <li>setTimeShiftPosition 5 
 * <li>wait 2s
 * <li>returnToLive
 * <li>select S3
 * </pre>
 */
public class DtvFixture {

    private static final int TIMESHIFT_DEPTH = 30 * 60 * 1000;
    private Service liveService;

    public void selectServiceByNumber(int nb) {
	System.out.println("DtvFixture.selectServiceByNumber() " + nb);
	ServiceList all = SIManager.createInstance().filterServices(null);
	Service service = all.getService(nb);

	if (service == null)
	    throw new IllegalArgumentException("Service " + nb + " does not exist");

	/* ServiceContext id 0 is the one configured for live in ms.xml */
	ServiceContext ctx = Utils.getServiceContext(0);
	ctx.select(service);
	liveService = service;
    }

    public int getServiceNumber(int index) {
	ServiceList all = SIManager.createInstance().filterServices(null);
	Service service = all.getService(index);

	if (service instanceof ServiceNumber)
	    return ((ServiceNumber) service).getServiceNumber();

	return -1;
    }

    public String getServiceName(int index) {
	ServiceList all = SIManager.createInstance().filterServices(null);
	Service service = all.getService(index);

	return service.getName();
    }

    public String getServiceLocator(int index) {
	ServiceList all = SIManager.createInstance().filterServices(null);
	Service service = all.getService(index);

	return service.getLocator().toExternalForm();
    }

    public void selectServiceByName(String name) {
	System.out.println("DtvFixture.selectServiceByName() " + name);
	ServiceNameFilter snf = new ServiceNameFilter(name);
	ServiceList namelist = SIManager.createInstance().filterServices(snf);

	if (namelist.size() == 0)
	    throw new IllegalArgumentException("Service " + name + " does not exist");

	Service service = namelist.getService(0);

	/* ServiceContext id 0 is the one configured for live in ms.xml */
	ServiceContext ctx = Utils.getServiceContext(0);
	ctx.select(service);
	liveService = service;
    }

    public void selectServiceByLocator(String locatorString) {
	System.out.println("DtvFixture.selectServiceByLocator() " + locatorString);
	Locator locator;
	try {
	    locator = LocatorFactory.getInstance().createLocator(locatorString);
	    Service service = SIManager.createInstance().getService(locator);

	    if (service == null)
		throw new IllegalArgumentException("Service " + locatorString + " does not exist");

	    /* ServiceContext id 0 is the one configured for live in ms.xml */
	    ServiceContext ctx = Utils.getServiceContext(0);
	    ctx.select(service);
	    liveService = service;
	} catch (MalformedLocatorException e) {
	    throw new RuntimeException(e);
	} catch (InvalidLocatorException e) {
	    throw new RuntimeException(e);
	}
    }
    public void pauseForSeconds(int s) {
	try {
        Thread.sleep((long)s * 1000);
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    /**
     * Timeshift is always on currently seected service
     */
    public void startTimeShiftRecording() {
	System.out.println("DtvFixture.startTimeShiftRecording()");
	Service service = selectedService();

	if (service == null)
	    throw new IllegalStateException("Select service before time shifting");

	Locator locator = service.getLocator();
	Record record = new Record(locator, new Date(), TIMESHIFT_DEPTH);

	try {
	    TimeShiftingUtilities.record(record);
	} catch (IOException e) {
	    throw new RuntimeException(e);
	} catch (CannotProcessException e) {
	    throw new RuntimeException(e);
	}
    }

    /**
     * stop recording timeshift buffer
     */
    public void stopTimeShiftRecording() {
	System.out.println("DtvFixture.stopTimeShiftRecording()");
	try {
	    Record[] timeshiftRecordings = TimeShiftingUtilities.getRecordContext().listRecords(null);

	    for (int i = 0; i < timeshiftRecordings.length; i++) {
		TimeShiftingUtilities.stopRecord(timeshiftRecordings[i]);
	    }

	} catch (IOException e) {
	    throw new RuntimeException(e);
	} catch (CannotProcessException e) {
	    throw new RuntimeException(e);
	}
    }

    public int selectedServiceNb() {
	System.out.println("DtvFixture.getSelectedService() ");

	ServiceContext ctx = Utils.getServiceContext(0);
	Service service = ctx.getService();
	return ((ServiceNumber) service).getServiceNumber();
    }

    public String selectedServiceLocator() {
	System.out.println("DtvFixture.getSelectedService() ");

	ServiceContext ctx = Utils.getServiceContext(0);
	Service service = ctx.getService();
	return service.getLocator().toExternalForm();
    }

    public Service selectedService() {
	System.out.println("DtvFixture.getSelectedService() ");

	ServiceContext ctx = Utils.getServiceContext(0);
	Service service = ctx.getService();
	return service;
    }

    public String selectedServiceName() {
	System.out.println("DtvFixture.getSelectedService() ");

	ServiceContext ctx = Utils.getServiceContext(0);
	Service service = ctx.getService();
	return service.getName();
    }

    public void playbackAtPosition(double postionInSeconds) {
	System.out.println("DtvFixture.playbackAtPosition() " + postionInSeconds);
	Player player = geTimeShiftPlayer();

	player.setMediaTime(new Time(postionInSeconds));
    }

    public void playbackAtSpeed(String rate) {
	System.out.println("DtvFixture.playbackAtSpeed()");
	Player player = geTimeShiftPlayer();

	
	player.setRate(Float.parseFloat(rate));
    }

    public Record getTimeShiftRecord() {
	System.out.println("DtvFixture.getTimeShiftRecord()");

	try {
	    Record[] timeshiftRecordings = TimeShiftingUtilities.getRecordContext().listRecords(null);

	    if (timeshiftRecordings.length == 0)
		return null;

	    return timeshiftRecordings[0];
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
    }

    /**
     * Make sure RecordedServiceContentManager is in ms.xml for serviceontext 0
     * 
     * @param position
     *            playback position
     * @param rate
     *            playback rate
     */
    public void playbackAtPositionAndRate(double positionInSeconds, String rate) {
	System.out.println("DtvFixture.playbackAtPositionAndRate()");
	Record timeShiftRecord = getTimeShiftRecord();

	try {
	    Time start = new Time((double) positionInSeconds);

	    Locator locator = new RecordedLocator(timeShiftRecord.getMediaLocator(), start, Duration.DURATION_UNKNOWN,
                Float.parseFloat(rate));
	    Service service = SIManager.createInstance().getService(locator);

	    Utils.getServiceContext(0).select(service);

	} catch (InvalidLocatorException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    /**
     * Make sure RecordedServiceContentManager is in ms.xml for serviceontext 0
     * 
     * @param position
     *            playback position
     * @param rate
     *            playback rate
     */
    public void returnToLive() {
	System.out.println("DtvFixture.returnToLive()");
	Player player = geTimeShiftPlayer();

	if (player == null)
	    return;

	selectServiceByLocator(liveService.getLocator().toExternalForm());
    }

    /**
     * Stop playing timeshift and return to live Timeshift must have been
     * started with {@link DtvFixture#playbackAtPositionAndRate(double, float)}
     */
    public void stopTimeShiftPlayback() {
	System.out.println("DtvFixture.stopTimeShiftPlayback()");
	Player player = geTimeShiftPlayer();
	player.stop();
    }

    public void playbackAtEnd() {
	System.out.println("DtvFixture.playbackAtEnd()");
	Record timeShiftRecord = getTimeShiftRecord();
	Time end = new Time((double) (timeShiftRecord.getDuration() / 1000));
	double playPos = end.getSeconds() - 5;

	playbackAtPositionAndRate(playPos, 1.f);
    }

    public void setTimeShiftRate(String rate) {
	System.out.println("DtvFixture.setTimeShiftRate()");
	Player player = geTimeShiftPlayer();
	player.setRate(Float.parseFloat(rate));
    }

    public void setTimeShiftPosition(double postionInSeconds) {
	System.out.println("DtvFixture.setTimeShiftPosition()");
	Player player = geTimeShiftPlayer();
	player.setMediaTime(new Time(postionInSeconds));
    }

    private Player geTimeShiftPlayer() {
	Record timeShiftRecord = getTimeShiftRecord();

	if (timeShiftRecord == null)
	    throw new RuntimeException("Timeshidt is not started");

	try {
	    DefaultServiceContext ctx = Utils.getServiceContext(0);
	    Service service = ctx.getService();

	    if (service == null)
		throw new RuntimeException("No service selected");

	    RecordedLocator rl = new RecordedLocator(timeShiftRecord.getMediaLocator());

	    if (LocatorManager.getInstance().equalsService(service.getLocator(), rl))
		throw new IllegalStateException("Current service different from timeshifted service");

	    Player player = getPlayer(ctx);

	    if (player == null)
		throw new IllegalStateException("Null timeshift player");

	    return player;

	} catch (InvalidLocatorException e) {
	    throw new RuntimeException(e);
	}
    }

    private Player getPlayer(ServiceContext ctx) {
	ServiceContentHandler[] sch = ctx.getServiceContentHandlers();

	for (int i = 0; i < sch.length; i++) {
	    if (sch[i] instanceof Player)
		return (Player) sch[i];
	}

	return null;
    }

}
