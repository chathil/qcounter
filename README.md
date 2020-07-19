
# Project Structure
```
        qcounter
        ...
        ├── src
            ├── main
            │   ├── java
            │   │   └── com
            │   │       └── proximity
            │   │           └── labs
            │   │               └── qcounter
            │   │                   ├── advice
            │   │                   ├── annotation
            │   │                   ├── cache
            │   │                   ├── config
            │   │                   ├── controllers
            │   │                   ├── data
            │   │                   │   ├── dto
            │   │                   │   │   ├── request
            │   │                   │   │   └── response
            │   │                   │   ├── models
            │   │                   │   │   ├── audit
            │   │                   │   │   ├── token
            │   │                   │   │   └── user
            │   │                   │   └── repositories
            │   │                   ├── event
            │   │                   │   └── listener
            │   │                   ├── exception
            │   │                   ├── security
            │   │                   ├── service
            │   │                   └── util
            │   └── resources
            │       ├── static
            │       └── templates
            ...
```
# Project Structure Explained

## ```com.proximy.labs.qcounter```

#### ```.advice```

contains classes with methods to throw proper error message. this classes are grouped based on their purpose, like ```AuthControllerAdvice.java``` contains methods to throw auth related errors.

#### ```.annotation```

contains files that combine annotations or create a new one.

#### ```.cache```

#### ```.config```

#### ```.controllers```

#### ```.event```

#### ```.exception```

#### ```.security```

All the packages above are self explanatory.

#### ```.data```

###### ```.dto```

contains classes used to exchange information client-server

###### ```.models```

contains database entity classes and it's helpers.

###### ```.repositories```

contains interface to write queries. each entity must have a repostory.

###### ```.service```

contains classes to write bussiness logic, usually before making changes to db. but this can also be used to write any other bussiness logic that doesn't involes db.



# Enpoint Documentations



# FAQ

1. where to query db?

   1. create abstract query method in an entity respected repository interface.

      Example:

```java
        public interface UserRepository extends JpaRepository<User, Long> {
            ...
            Optional<User> findByEmail(String email);
            ...
        }
```

   2. create a query method in an entity  respected service class

      Example:

```java
        public class UserService implements UserDetailsService{

            private static final Logger logger = Logger.getLogger(UserService.class);
            private final UserRepository userRepository;
            private final UserDeviceService userDeviceService;
            private final RefreshTokenService refreshTokenService;

            @Autowired
            public UserService(UserRepository userRepository, UserDeviceService userDeviceService, RefreshTokenService refreshTokenService) {
                this.userRepository = userRepository;
                this.userDeviceService = userDeviceService;
                this.refreshTokenService = refreshTokenService;
            }

            /**
            * Finds a user in the database by email
            */
            public Optional<User> findByEmail(String email) {
                return userRepository.findByEmail(email);
            }
            ...
        }
```

3. Inject the service class to where you need it. like in a controller class.

   Example on how to inject:

   ```java
   public class UserController {
     private static final Logger logger = Logger.getLogger(UserController.class);
   
     private final AuthService authService;
   
     private final UserService userService;
   
     private final ApplicationEventPublisher applicationEventPublisher;
     
     @Autowired
     public UserController(AuthService authService, UserService userService, ApplicationEventPublisher applicationEventPublisher) {
         this.authService = authService;
         this.userService = userService;
         this.applicationEventPublisher = applicationEventPublisher;
     }
   
   ```

   ​		