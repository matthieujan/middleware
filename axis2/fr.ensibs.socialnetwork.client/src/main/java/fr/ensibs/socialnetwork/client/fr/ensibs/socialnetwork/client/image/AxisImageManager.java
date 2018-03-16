package fr.ensibs.socialnetwork.client.fr.ensibs.socialnetwork.client.image;

import fr.ensibs.socialnetwork.configuration.ConfigurationManager;
import fr.ensibs.socialnetwork.logic.image.ImageManager;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.Base64;

public class AxisImageManager implements ImageManager {

    public String addImage(Image image) throws Exception {
        String toSend = convertToBytes(image);
        String message = axisCall("put",toSend);
        return message;
    }

    public Image getImage(String s) throws Exception {
        String message = axisCall("get",s);
        return convertFromBytes(message);
    }

    public void close() throws IOException {
        //because
    }


    private String convertToBytes(Image object) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String result = null;
        try {
            ImageIO.write((BufferedImage) object,"png",baos);
            result = Base64.getEncoder().encodeToString(baos.toByteArray());
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Image convertFromBytes(String bytes) throws IOException, ClassNotFoundException {
        byte[] byteArray = Base64.getDecoder().decode(bytes);
        BufferedImage image= ImageIO.read(new ByteArrayInputStream(byteArray));
        return image;
    }

    private String axisCall(String func, String message){
        String ret = null;


        String host = ConfigurationManager.getInstance().getProperty(ConfigurationManager.SERVER_HOST,"localhost");
        int port = ConfigurationManager.getInstance().getIntegerProperty(ConfigurationManager.AXIS2_PORT,5002);

        EndpointReference targetEPR = new EndpointReference("http://"+host+":"+port+"/axis2/services/ImageService");
        try {
            Options options = new Options();
            options.setTo(targetEPR);
            options.setTransportInProtocol(Constants.TRANSPORT_HTTP);
            ServiceClient sender = new ServiceClient();
            sender.setOptions(options);
            OMFactory fac = OMAbstractFactory.getOMFactory();
            OMNamespace omNs = fac.createOMNamespace("http://services.server.socialnetwork.ensibs.fr", "tns");
            OMElement method = fac.createOMElement(func, omNs);
            OMElement value = fac.createOMElement("message", omNs);
            value.addChild(fac.createOMText(message));
            method.addChild(value);
            OMElement result = sender.sendReceive(method);
            ret = result.getFirstElement().getText();
        }catch (Exception e) {
            e.printStackTrace();
        }
            return ret;
    }
}
