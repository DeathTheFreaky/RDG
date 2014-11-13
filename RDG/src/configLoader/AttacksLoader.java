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

import general.Enums.Attributes;
import general.Enums.ItemClasses;

/**Tries to parse XML Config File.
 * Checks if values from config are valid and throws error on failure.
 * 
 * @author Flo
 *
 */
public class AttacksLoader {

	/**Tries to parse XML Config File.
	 * Checks if values from config are valid and throws error on failure.
	 * 
	 * @param configpath
	 * @return AttackTemplates
	 * @throws IllegalArgumentException
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @see AttacksLoader
	 */
	public static Map<String, AttackTemplate> run(String configpath) throws IllegalArgumentException, ParserConfigurationException, SAXException, IOException {
			
		Map<String, AttackTemplate> attackTemplates = new HashMap<String, AttackTemplate>();
		
		File fXmlFile = new File(configpath + "Attacks.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		
		doc.getDocumentElement().normalize();
		
		NodeList nList = doc.getElementsByTagName("Attack");
		 
		for (int temp = 0; temp < nList.getLength(); temp++) {
	 
			Node nNode = nList.item(temp);
	 
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	 
				Element eElement = (Element) nNode;
				
				//store current element's values
				String name, effectStr;
				Attributes effect;
				float statsLowMultiplier, statsHighMultiplier, hpDamage, hitProbability, x;
				
				name = eElement.getElementsByTagName("Name").item(0).getTextContent();
				statsLowMultiplier = Float.parseFloat(eElement.getElementsByTagName("Stats_Low_Multiplier").item(0).getTextContent());
				statsHighMultiplier = Float.parseFloat(eElement.getElementsByTagName("Stats_High_Multiplier").item(0).getTextContent());
				hpDamage = Float.parseFloat(eElement.getElementsByTagName("HP_Damage").item(0).getTextContent());
				hitProbability = Float.parseFloat(eElement.getElementsByTagName("Hit_Probability").item(0).getTextContent());
				x = Float.parseFloat(eElement.getElementsByTagName("x").item(0).getTextContent());
				effectStr = eElement.getElementsByTagName("Effect").item(0).getTextContent();
				
				//check if parsed values are valid and set enums
				if (name.length() == 0) throw new IllegalArgumentException("Invalid Name \"" + name + "\" at Attack \"" + name + "\"");
				
				if (effectStr.equals("hp")) effect = Attributes.HP;
				else if (effectStr.equals("speed")) effect = Attributes.SPEED;
				else if (effectStr.equals("accuracy")) effect = Attributes.ACCURACY;
				else if (effectStr.equals("strength")) effect = Attributes.STRENGTH;
				else {
					throw new IllegalArgumentException("Invalid Effect \"" + effectStr + "\" at Attack \"" + name + "\"");
				}
				
				if (statsLowMultiplier < 0) throw new IllegalArgumentException("Invalid Stats Low Multiplier \"" + statsLowMultiplier + "\" at Attack \"" + name + "\"");
				if (statsHighMultiplier < 0) throw new IllegalArgumentException("Invalid Stats High Multiplier \"" + statsHighMultiplier + "\" at Attack \"" + name + "\"");
				if (hpDamage < 0) throw new IllegalArgumentException("Invalid HP Damage \"" + hpDamage + "\" at Attack \"" + name + "\"");
				if (hitProbability < 0) throw new IllegalArgumentException("Invalid Hit Probability \"" + hitProbability + "\" at Attack \"" + name + "\"");
				if (x < 0) throw new IllegalArgumentException("Invalid x \"" + x + "\" at Attack \"" + name + "\"");
				
				
				//put template on list of available templates
				attackTemplates.put(name, new AttackTemplate(name, effect, statsLowMultiplier, statsHighMultiplier, hpDamage, hitProbability, x));
			}
		}
		
		return attackTemplates;
	}
}
