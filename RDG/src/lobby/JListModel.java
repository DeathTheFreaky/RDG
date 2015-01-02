package lobby;

import java.util.ArrayList;

import javax.swing.AbstractListModel;

class JListModel extends AbstractListModel { 

    private static final long serialVersionUID = 1L; 
    private ArrayList<String> data; 

    public JListModel() { 
        data = new ArrayList<String>(); 
        data.add("eins"); 
        data.add("zwei"); 
        data.add("drei"); 
        data.add("vier"); 
    } 

    public int getSize() { 
        return data.size(); 
    } 

    public Object getElementAt(int index) { 
        return data.get(index); 
    } 

    public Object getIndex(Object o) { 
        return data.indexOf(o); 
    } 

    public void setElement(String s, int index) { 
        data.set(index, s); 
        update(0, this.getSize()); 
    } 

    public void addElement(String s) { 
        data.add(s); 
        update(this.getSize() - 1, this.getSize()); 
    } 
     
    public void removeElement(int index){ 
        data.remove(index); 
        update(0, this.getSize()); 
    } 

    public void update(int von, int bis) { 
        fireIntervalAdded(this, von, bis); 
    } 
}
