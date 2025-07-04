package checker.pwd.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document("test")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Model {
    @Id private String id;

    @NonNull String data;
}
