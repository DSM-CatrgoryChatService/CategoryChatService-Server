package com.example.chat_example_with_socketio.service.friends;

import com.example.chat_example_with_socketio.entity.friends.Friends;
import com.example.chat_example_with_socketio.entity.friends.repository.FriendsRepository;
import com.example.chat_example_with_socketio.entity.image.background.BackgroundImage;
import com.example.chat_example_with_socketio.entity.image.background.repository.BackgroundImageRepository;
import com.example.chat_example_with_socketio.entity.image.user.UserImage;
import com.example.chat_example_with_socketio.entity.image.user.repository.UserImageRepository;
import com.example.chat_example_with_socketio.entity.user.User;
import com.example.chat_example_with_socketio.entity.user.repository.UserRepository;
import com.example.chat_example_with_socketio.error.exceptions.UserImageNotFoundException;
import com.example.chat_example_with_socketio.error.exceptions.UserNotFoundException;
import com.example.chat_example_with_socketio.payload.response.FriendsResponse;
import com.example.chat_example_with_socketio.security.JwtProvider;
import com.example.chat_example_with_socketio.service.friends.FriendsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendsServiceImpl implements FriendsService {

    private final UserRepository userRepository;
    private final FriendsRepository friendsRepository;
    private final UserImageRepository userImageRepository;
    private final BackgroundImageRepository backgroundImageRepository;

    private final JwtProvider jwtProvider;

    @Override
    public void addFriends(String token, String userInfo) {
        User user = userRepository.findByUserId(jwtProvider.getUserId(token))
                .orElseThrow(UserNotFoundException::new);

        User friend = userRepository.findByInfo(userInfo)
                .orElseThrow(UserNotFoundException::new);

        friendsRepository.save(
                Friends.builder()
                .friendId(friend.getUserId())
                .userId(user.getUserId())
                .isFriendly(false)
                .build()
        );
    }

    @Override
    public void blockFriend(String token, String userInfo, boolean isFriendly) {
        User user = userRepository.findByUserId(jwtProvider.getUserId(token))
                .orElseThrow(UserNotFoundException::new);

        User friend = userRepository.findByInfo(userInfo)
                .orElseThrow(UserNotFoundException::new);

        Friends friends = friendsRepository.findByFriendIdAndUserId(friend.getUserId(), user.getUserId())
                .orElseThrow(UserNotFoundException::new);

        if(!friends.isFriendly() == isFriendly) {
            friendsRepository.save(
                    friends.block(isFriendly)
            );
        }
    }

    @Override
    public List<FriendsResponse> getFriends(String token) {
        User user = userRepository.findByUserId(jwtProvider.getUserId(token))
                .orElseThrow(UserNotFoundException::new);

        UserImage userImage = userImageRepository.findByUserId(user.getUserId())
                .orElseThrow(UserImageNotFoundException::new);

        BackgroundImage myBackgroundImage = backgroundImageRepository.findByUserId(user.getUserId())
                .orElseThrow(UserImageNotFoundException::new);

        List<FriendsResponse> responses = new ArrayList<>();
        List<Friends> friendsList = friendsRepository.findAllByUserId(user.getUserId());

        responses.add(
                FriendsResponse.builder()
                        .name(user.getName())
                        .friendsInfo(user.getInfo())
                        .isFriendly(false)
                        .sex(user.getSex())
                        .userImageName(userImage.getImageName())
                        .backgroundImageName(myBackgroundImage.getImageName())
                        .isMine(true)
                        .build()
    );

        for(Friends friends : friendsList) {
            if (!friends.isFriendly()) {
                User friend = userRepository.findByUserId(friends.getFriendId())
                        .orElseThrow(UserNotFoundException::new);

                UserImage friendImage = userImageRepository.findByUserId(friend.getUserId())
                        .orElseThrow(UserImageNotFoundException::new);

                BackgroundImage backgroundImage = backgroundImageRepository.findByUserId(friend.getUserId())
                        .orElseThrow(UserImageNotFoundException::new);

                FriendsResponse friendsResponse = FriendsResponse.builder()
                        .friendsInfo(friend.getInfo())
                        .isFriendly(friends.isFriendly())
                        .name(friend.getName())
                        .userImageName(friendImage.getImageName())
                        .sex(friend.getSex())
                        .backgroundImageName(backgroundImage.getImageName())
                        .isMine(false)
                        .build();

                responses.add(friendsResponse);
            }
        }

        return responses;
    }

    @Override
    public List<FriendsResponse> getBlockFriends(String token) {
        User user = userRepository.findByUserId(jwtProvider.getUserId(token))
                .orElseThrow(UserNotFoundException::new);

        List<FriendsResponse> responses = new ArrayList<>();
        List<Friends> friendsList = friendsRepository.findAllByUserId(user.getUserId());

        for(Friends friends : friendsList) {
            if (friends.isFriendly()) {
                User friend = userRepository.findByUserId(friends.getUserId())
                        .orElseThrow(UserNotFoundException::new);

                UserImage friendImage = userImageRepository.findByUserId(friend.getUserId())
                        .orElseThrow(UserImageNotFoundException::new);

                BackgroundImage backgroundImage = backgroundImageRepository.findByUserId(friend.getUserId())
                        .orElseThrow(UserImageNotFoundException::new);

                FriendsResponse friendsResponse = FriendsResponse.builder()
                        .friendsInfo(friend.getInfo())
                        .isFriendly(friends.isFriendly())
                        .name(friend.getName())
                        .userImageName(friendImage.getImageName())
                        .sex(friend.getSex())
                        .backgroundImageName(backgroundImage.getImageName())
                        .build();

                if(friend.getUserId().equals(user.getUserId())) {
                    responses.add(0, friendsResponse);
                }else {
                    responses.add(friendsResponse);
                }
            }
        }

        return responses;
    }

    @Override
    @Transactional
    public void deleteFriends(String token, String userInfo) {
        User user = userRepository.findByUserId(jwtProvider.getUserId(token))
                .orElseThrow(UserNotFoundException::new);

        User friend = userRepository.findByInfo(userInfo)
                .orElseThrow(UserNotFoundException::new);

        friendsRepository.deleteByFriendIdAndUserId(friend.getUserId(), user.getUserId());
    }
}
