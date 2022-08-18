package com.cg.service.user;

import com.cg.model.LocationRegion;
import com.cg.model.User;
import com.cg.model.UserPrinciple;
import com.cg.model.dto.UserDTO;
import com.cg.repository.LocationRegionRepository;
import com.cg.repository.UserRepository;
import com.cg.service.locationRegion.LocationRegionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LocationRegionRepository locationRegionRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
    //Hiển thị list tất cả ra
    @Override
    public List<UserDTO> findAllUserDTOByDeletedIsFalse() {
        return userRepository.findAllUserDTOByDeletedIsFalse();
    }

    //Hàm search
    @Override
    public List<UserDTO> searchAllUser(String keywork) {
        return userRepository.search(keywork);
    }


    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User getById(Long id) {
        return userRepository.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

//    @Override
//    public Optional<UserDTO> findUserDTOByUsername(String username) {
//        return userRepository.findUserDTOByUsername(username);
//    }

    @Override
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        LocationRegion locationRegion = locationRegionRepository.save(user.getLocationRegion());

        user.setLocationRegion(locationRegion);

        return userRepository.save(user);
    }

    @Override
    public User saveUpdate(User user) {
        LocationRegion locationRegion = locationRegionRepository.save(user.getLocationRegion());

        user.setLocationRegion(locationRegion);

        return userRepository.save(user);
    }

    @Override
    public void remove(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException(username);
        }
        return UserPrinciple.build(userOptional.get());
//        return (UserDetails) userOptional.get();

    }
    @Override
    public Boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    @Override
    public Boolean existsByUsernameAndIdIsNot(String username, Long id) {
        return userRepository.existsByUsernameAndIdIsNot(username, id);
    }

    @Override
    public Boolean existsByPhoneAndIdIsNot(String phone, Long id) {
        return userRepository.existsByPhoneAndIdIsNot(phone, id);
    }


}
