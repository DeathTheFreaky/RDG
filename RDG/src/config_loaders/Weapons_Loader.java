package config_loaders;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import config_templates.Weapon_Template;

public class Weapons_Loader {

	private String configpath;
	private Map<String, Weapon_Template> weapon_templates;
	
	public Weapons_Loader(String configpath) {
		this.configpath = configpath;
		weapon_templates = new HashMap<String, Weapon_Template>();
	}
	
	/**
	 * Tries to parse XML Config File
	 * Checks if values from config are valid and throw error on failure
	 * 
	 * @return
	 * @throws IllegalArgumentException
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public Map<String, Weapon_Template> run() throws IllegalArgumentException, ParserConfigurationException, SAXException, IOException {
		
		try {
			
			File fXmlFile = new File(configpath + "Weapons.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			
			doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName("Weapon");
			 
			for (int temp = 0; temp < nList.getLength(); temp++) {
		 
				Node nNode = nList.item(temp);
		 
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		 
					Element eElement = (Element) nNode;
					
					//store current element's values
					String name, item_class, type, image_big, image_small;
					float class_multiplier, stats_low_multiplier, stats_high_multiplier, attack, speed, accuracy, defence, slots, max;
					
					name = eElement.getElementsByTagName("Name").item(0).getTextContent();
					item_class = eElement.getElementsByTagName("Item_Class").item(0).getTextContent();
					type = eElement.getElementsByTagName("Type").item(0).getTextContent();
					image_big = eElement.getElementsByTagName("Image_Big").item(0).getTextContent();
					image_small = eElement.getElementsByTagName("Image_Small").item(0).getTextContent();
					stats_low_multiplier = Float.parseFloat(eElement.getElementsByTagName("Stats_Low_Multiplier").item(0).getTextContent());
					stats_high_multiplier = Float.parseFloat(eElement.getElementsByTagName("Stats_High_Multiplier").item(0).getTextContent());
					class_multiplier = Float.parseFloat(eElement.getElementsByTagName("Class_Multiplier").item(0).getTextContent());
					attack = Float.parseFloat(eElement.getElementsByTagName("Attack").item(0).getTextContent());
					speed = Float.parseFloat(eElement.getElementsByTagName("Speed").item(0).getTextContent());
					accuracy = Float.parseFloat(eElement.getElementsByTagName("Accuracy").item(0).getTextContent());
					defence = Float.parseFloat(eElement.getElementsByTagName("Defence").item(0).getTextContent());
					slots = Float.parseFloat(eElement.getElementsByTagName("Slots").item(0).getTextContent());
					max = Float.parseFloat(eElement.getElementsByTagName("Max").item(0).getTextContent());

					//check if parsed values are valid and set enums
					
					//put template on list of available templates
					weapon_templates.put(name, new Weapon_Template(name, item_class, type, image_big, image_small, 
							class_multiplier, stats_low_multiplier, stats_high_multiplier, attack, speed, accuracy, defence, slots, max));
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
	    }
		
		return weapon_templates;
	}
}

