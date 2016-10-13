package functionalities;

import org.springframework.data.mongodb.repository.MongoRepository;
import dataStructure.Employee;

public interface EmployeeRepository extends MongoRepository<Employee, String>{
	
	//Declare the methods that are needs implementing
	

}
