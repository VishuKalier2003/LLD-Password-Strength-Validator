package checker.pwd.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import checker.pwd.domain.model.Model;

public interface DataRepo extends MongoRepository<Model, String> {
    
}
