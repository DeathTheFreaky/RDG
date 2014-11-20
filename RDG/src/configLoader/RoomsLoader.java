package configLoader;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import general.Enums.Attributes;
import general.Enums.ItemClasses;
import general.Enums.Levels;
import general.Enums.Modes;
import general.Enums.RoomTypes;
import general.Enums.Targets;

/**Tries to parse XML Config File.
 * Checks if values from config are valid and throws error on failure.
 * 
 * @author Flo
 *
 */
public class RoomsLoader {

	/**Tries to parse XML Config File.
	 * Checks if values from config are valid and throws error on failure.
	 * 
	 * @param configpath
	 * @return RoomTemplates
	 * @throws IllegalArgumentException
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @see RoomsLoader
	 */
	public static Map<RoomTypes, RoomTemplate> run(String configpath) throws IllegalArgumentException, ParserConfigurationException, SAXException, IOException {
		
		 Map<RoomTypes, RoomTemplate> roomTemplates = new HashMap<RoomTypes, RoomTemplate>();
			
		File fXmlFile = new File(configpath + "Rooms.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		
		doc.getDocumentElement().normalize();
		
		NodeList nList = doc.getElementsByTagName("Room");
		
		for (int temp = 0; temp < nList.getLength(); temp++) {
	 
			Node nNode = nList.item(temp);
	 
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	 
				Element eElement = (Element) nNode;
				
				//allocate memory and store current element's values
				RoomTypes type = null;
				String name, description, image;
				float itemMultiplier;
				int monsterCount, itemCount;
				Map<Levels, Float> monster; 
				Map<ItemClasses, Float> find_probabilities;
				boolean[] doorPositions; //0: N, 1: E, 2: S, 3: W
 
 					monster = new HashMap<Levels, Float>();
 					find_probabilities = new HashMap<ItemClasses, Float>();
 					doorPositions = new boolean[4];
					
				name = eElement.getElementsByTagName("Name").item(0).getTextContent();
				description = eElement.getElementsByTagName("Description").item(0).getTextContent();
				image = eElement.getElementsByTagName("Image").item(0).getTextContent();
				monsterCount = Integer.parseInt(eElement.getElementsByTagName("Monster_Count").item(0).getTextContent());
				itemMultiplier = Float.parseFloat(eElement.getElementsByTagName("Item_Multiplier").item(0).getTextContent());
				itemCount = Integer.parseInt(eElement.getElementsByTagName("Item_Count").item(0).getTextContent());
				
				//check if parsed values are valid and set enums
				if (name.equals("Dead End")) type = RoomTypes.DEADEND;
				else if (name.equals("Hallway")) type = RoomTypes.HALLWAY;
				else if (name.equals("Turn")) type = RoomTypes.TURN;
				else if (name.equals("Junction")) type = RoomTypes.JUNCTION;
				else if (name.equals("T-Junction")) type = RoomTypes.TJUNCTION;
				else if (name.equals("Treasure Chamber")) type = RoomTypes.TREASURECHAMBER;
				else throw new IllegalArgumentException("Invalid RoomType \"" + name + "\" at Room \"" + name + "\"");
				
				if (name.length() == 0) throw new IllegalArgumentException("Invalid Name \"" + name + "\" at Room \"" + name + "\"");
				
				if (monsterCount < 0) throw new IllegalArgumentException("Invalid Monster Count \"" + monsterCount + "\" at Room \"" + name + "\"");
				if (itemMultiplier < 0) throw new IllegalArgumentException("Invalid Item Multiplier \"" + itemMultiplier + "\" at Room \"" + name + "\"");
				if (itemCount < 0) throw new IllegalArgumentException("Invalid Item Count \"" + itemCount + "\" at Room \"" + name + "\"");
									
				//process elements on lower hierarchy Levels
				
					//read monster placing probabilities
					NodeList monster_list = (NodeList) eElement.getElementsByTagName("Monster").item(0);
					int monster_length = monster_list.getLength();
					
					for (int monster_temp = 0; monster_temp < monster_length; monster_temp++) {
						Node monster_node = monster_list.item(monster_temp);
						if (monster_node.getNodeType() == Node.ELEMENT_NODE) {
							
							if (Float.parseFloat(monster_node.getTextContent()) < 0) throw new IllegalArgumentException("Invalid Monster/" + monster_node.getNodeName() + " \"" + monster_node.getTextContent() + "\" at Room \"" + name + "\"");
							
							String monsterProbability = monster_node.getNodeName();
							Levels monsterLevel = null;
							
							if (monsterProbability.equals("no")) {
								monsterLevel = Levels.NONE;
							} else if (monsterProbability.equals("easy")) {
								monsterLevel = Levels.EASY;
							} else if (monsterProbability.equals("normal")) {
								monsterLevel = Levels.NORMAL;
							} else if (monsterProbability.equals("hard")) {
								monsterLevel = Levels.HARD;
							}
							
							monster.put(monsterLevel,  Float.parseFloat(monster_node.getTextContent()));
						}
					}
					
					//read item finding probabilities
					NodeList find_prob_list = (NodeList) eElement.getElementsByTagName("Find_Probabilities").item(0);
					int find_prob_length = find_prob_list.getLength();
					
					for (int find_prob_temp = 0; find_prob_temp < find_prob_length; find_prob_temp++) {
						Node find_prob_node = find_prob_list.item(find_prob_temp);
						if (find_prob_node.getNodeType() == Node.ELEMENT_NODE) {
							
							if (Float.parseFloat(find_prob_node.getTextContent()) < 0) throw new IllegalArgumentException("Invalid Find Probability/" + find_prob_node.getNodeName() + " \"" + find_prob_node.getTextContent() + "\" at Room \"" + name + "\"");
								
							String itemProbability = find_prob_node.getNodeName();
							ItemClasses itemLevel = null;
							
							if (itemProbability.equals("no")) {
								itemLevel = ItemClasses.NONE;
							} else if (itemProbability.equals("easy")) {
								itemLevel = ItemClasses.WEAK;
							} else if (itemProbability.equals("normal")) {
								itemLevel = ItemClasses.MEDIUM;
							} else if (itemProbability.equals("hard")) {
								itemLevel = ItemClasses.STRONG;
							}
							
							find_probabilities.put(itemLevel,  Float.parseFloat(find_prob_node.getTextContent()));
						}
					}
					
					//read default door positions
					int n = 0, e = 0, s = 0, w = 0;
					
					NodeList door_pos_list = (NodeList) eElement.getElementsByTagName("Door_Positions").item(0);
					int door_pos_length = door_pos_list.getLength();
					
					for (int door_pos_temp = 0; door_pos_temp < door_pos_length; door_pos_temp++) {
						Node door_pos_node = door_pos_list.item(door_pos_temp);
						if (door_pos_node.getNodeType() == Node.ELEMENT_NODE) {
														
						
							if (door_pos_node.getNodeName().equals("N")) {
								n = Integer.parseInt(door_pos_node.getTextContent());
								if (n < 0 || n > 1) throw new IllegalArgumentException("Invalid Door Position/" + door_pos_node.getNodeName() + " \"" + door_pos_node.getTextContent() + "\" at Room \"" + name + "\"");
							}
							else if (door_pos_node.getNodeName().equals("E")) {
								e = Integer.parseInt(door_pos_node.getTextContent());
								if (e < 0 || e > 1) throw new IllegalArgumentException("Invalid Door Position/" + door_pos_node.getNodeName() + " \"" + door_pos_node.getTextContent() + "\" at Room \"" + name + "\"");
							}
							else if (door_pos_node.getNodeName().equals("S")) {
								s = Integer.parseInt(door_pos_node.getTextContent());
								if (s < 0 || s > 1) throw new IllegalArgumentException("Invalid Door Position/" + door_pos_node.getNodeName() + " \"" + door_pos_node.getTextContent() + "\" at Room \"" + name + "\"");
							}
							else if (door_pos_node.getNodeName().equals("W")) {
								w = Integer.parseInt(door_pos_node.getTextContent());
								if (w < 0 || w > 1) throw new IllegalArgumentException("Invalid Door Position/" + door_pos_node.getNodeName() + " \"" + door_pos_node.getTextContent() + "\" at Room \"" + name + "\"");
							}
						}
					}
					
					doorPositions[0] = n == 1;
					doorPositions[1] = e == 1;
					doorPositions[2] = s == 1;
					doorPositions[3] = w == 1;	
										
					//put template on list of available templates
					roomTemplates.put(type, new RoomTemplate(type, description, image, monsterCount, itemMultiplier, itemCount, 
							monster, find_probabilities, doorPositions));
				}
		}
		
		return roomTemplates;
	}
}
