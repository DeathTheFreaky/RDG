package configLoader;

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

import general.Enums.ItemClasses;

/**Tries to parse XML Config File.
 * Checks if values from config are valid and throws error on failure.
 * 
 * @author Flo
 *
 */
public class ArmamentsLoader {

	/**Tries to parse XML Config File.
	 * Checks if values from config are valid and throws error on failure.
	 * 
	 * @param configpath
	 * @return ArmamentTemplates
	 * @throws IllegalArgumentException
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @see ArmamentsLoader
	 */
	public static Map<String, ArmamentTemplate> run(String configpath) throws IllegalArgumentException, ParserConfigurationException, SAXException, IOException {
		
		Map<String, ArmamentTemplate> armamentTemplates = new HashMap<String, ArmamentTemplate>();
					
		File fXmlFile = new File(configpath + "Armaments.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		
		doc.getDocumentElement().normalize();
		
		NodeList nList = doc.getElementsByTagName("Armament");
		 
		for (int temp = 0; temp < nList.getLength(); temp++) {
	 
			Node nNode = nList.item(temp);
	 
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	 
				Element eElement = (Element) nNode;
				
				//store current element's values
				String name, itemClassStr, type, image;
				ItemClasses itemClass;
				float classMultiplier, statsLowMultiplier, statsHighMultiplier, armor, speed, bonus;
				
				name = eElement.getElementsByTagName("Name").item(0).getTextContent();
				itemClassStr = eElement.getElementsByTagName("Item_Class").item(0).getTextContent();
				type = eElement.getElementsByTagName("Type").item(0).getTextContent();
				image = eElement.getElementsByTagName("Image").item(0).getTextContent();
				statsLowMultiplier = Float.parseFloat(eElement.getElementsByTagName("Stats_Low_Multiplier").item(0).getTextContent());
				statsHighMultiplier = Float.parseFloat(eElement.getElementsByTagName("Stats_High_Multiplier").item(0).getTextContent());
				classMultiplier = Float.parseFloat(eElement.getElementsByTagName("Class_Multiplier").item(0).getTextContent());
				armor = Float.parseFloat(eElement.getElementsByTagName("Armor").item(0).getTextContent());
				speed = Float.parseFloat(eElement.getElementsByTagName("Speed").item(0).getTextContent());
				bonus = Float.parseFloat(eElement.getElementsByTagName("Bonus").item(0).getTextContent());
				
				//check if parsed values are valid and set enums
				if (name.length() == 0) throw new IllegalArgumentException("Invalid Name \"" + name + "\" at Armament \"" + name + "\"");
				
				if (itemClassStr.equals("weak")) itemClass = ItemClasses.WEAK;
				else if (itemClassStr.equals("medium")) itemClass = ItemClasses.MEDIUM;
				else if (itemClassStr.equals("strong")) itemClass = ItemClasses.STRONG;
				else {
					throw new IllegalArgumentException("Invalid Item Class \"" + itemClassStr + "\" at Armament \"" + name + "\"");
				}
				
				if (classMultiplier < 0) throw new IllegalArgumentException("Invalid Class Multiplier \"" + classMultiplier + "\" at Armament \"" + name + "\"");
				if (statsLowMultiplier < 0) throw new IllegalArgumentException("Invalid Stats Low Multiplier \"" + statsLowMultiplier + "\" at Armament \"" + name + "\"");
				if (statsHighMultiplier < 0) throw new IllegalArgumentException("Invalid Stats High Multiplier \"" + statsHighMultiplier + "\" at Armament \"" + name + "\"");
				if (armor < 0) throw new IllegalArgumentException("Invalid Armor \"" + armor + "\" at Armament \"" + name + "\"");
				if (speed < 0) throw new IllegalArgumentException("Invalid Speed \"" + speed + "\" at Armament \"" + name + "\"");
				if (bonus < 0) throw new IllegalArgumentException("Invalid Bonus \"" + bonus + "\" at Armament \"" + name + "\"");
				
				//put template on list of available templates
				armamentTemplates.put(name, new ArmamentTemplate(name, type, image, itemClass, 
						classMultiplier, statsLowMultiplier, statsHighMultiplier, armor, speed, bonus));
			}
		}
			
		return armamentTemplates;
	}
}
