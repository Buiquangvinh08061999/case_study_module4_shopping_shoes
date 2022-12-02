package com.cg.service.user;

import com.cg.model.LocationRegion;
import com.cg.model.User;
import com.cg.model.UserPrinciple;
import com.cg.model.dto.CountDTO;
import com.cg.model.dto.UserDTO;
import com.cg.repository.LocationRegionRepository;
import com.cg.repository.UserRepository;
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

    @Override
    public List<UserDTO> findAllUserDTOByDeletedIsFalse() {
        return userRepository.findAllUserDTOByDeletedIsFalse();
    }


    @Override
    public List<UserDTO> searchAllUser(String keyword) {
        return userRepository.search(keyword);
    }


    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User getById(Long id) {
        return userRepository.getById(id);
    }

    /*Triển khai phương thức này từ repository nhận giá trị tên(user.getUsername) truyền vào ở hàm login(khi ta đăng nhập vào)  */
    @Override
    public User getByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /*Tìm id của user để kiểm tra có tồn tại không đưa vào phần CartRestController*/
    @Override
    public Optional<UserDTO> findUserDTOById(long id) {
        return userRepository.findUserDTOById(id);
    }


    @Override
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        LocationRegion locationRegion = locationRegionRepository.save(user.getLocationRegion());/*hàm save này được viết ở location để lưu thông tin location*/

        user.setLocationRegion(locationRegion);

        return userRepository.save(user);
    }


    @Override
    public User saveUpdate(User user) {
        LocationRegion locationRegion = locationRegionRepository.save(user.getLocationRegion()); /*hàm save này được viết ở location để lưu thông tin location*/

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

    /*Cập nhật cho trùng tên của nó, nhưng không trùng tên ở các mục khác*/
    @Override
    public Boolean existsByUsernameAndIdIsNot(String username, Long id) {
        return userRepository.existsByUsernameAndIdIsNot(username, id);
    }

    /*Cập nhật cho trùng phone của nó, nhưng không trùng phone ở các mục khác*/
    @Override
    public Boolean existsByPhoneAndIdIsNot(String phone, Long id) {
        return userRepository.existsByPhoneAndIdIsNot(phone, id);
    }

    /*Viết hàm này để truyền giá trị vào menu(show_cart), lấy giá đó để lấy id, tên người dùng(đăng nhập)*/
    @Override
    public Optional<UserDTO> findUserDTOByUsername(String username) {
        return userRepository.findUserDTOByUsername(username);
    }

    /*Hàm tìm kiếm tất cả các trường theo id tăng dần sortASC theo id*/
    @Override
    public List<UserDTO> findAllSortASCIdUserDTO() {
        return userRepository.findAllSortASCIdUserDTO();
    }

    @Override
    public List<UserDTO> findAllSortDESCIdUserDTO() {
        return userRepository.findAllSortDESCIdUserDTO();
    }

    @Override
    public List<UserDTO> findAllSortASCUserNameUserDTO() {
        return userRepository.findAllSortASCUserNameUserDTO();
    }

    @Override
    public List<UserDTO> findAllSortDESCUserNameUserDTO() {
        return userRepository.findAllSortDESCUserNameUserDTO();
    }

    @Override
    public List<UserDTO> findAllSortASCFullNameUserDTO() {
        return userRepository.findAllSortASCFullNameUserDTO();
    }

    @Override
    public List<UserDTO> findAllSortDESCFullNameUserDTO() {
        return userRepository.findAllSortDESCFullNameUserDTO();
    }

    /*Đếm tổng số lượng danh sách người dùng, đang tốn tại(Get api/customers/count)*/
    @Override
    public CountDTO findAllCount() {
        return userRepository.findAllCount();
    }

    @Override
    public List<UserDTO> findAllUserDTOHistoryByDeletedIsTrue() {
        return userRepository.findAllUserDTOHistoryByDeletedIsTrue();
    }


}
