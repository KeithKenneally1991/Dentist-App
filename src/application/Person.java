//Keith Kenneally DNET2 
package application;

import java.io.Serializable;
public class Person implements Serializable {

	 String name,address;
	 int contactNo;
	 Person(String n, String add){
		 this.name = n;
		 this.address = add;
	 }
}
