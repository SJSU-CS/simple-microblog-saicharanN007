package edu.sjsu.cmpe272.simpleblog.server;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;



public interface MessageRepository extends JpaRepository<Messageinfo, Long> {
    List<Messageinfo> findAllByOrderByMessageIdDesc();
    List<Messageinfo> findAllByOrderByMessageIdAsc();
}



