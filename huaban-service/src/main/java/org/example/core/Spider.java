package org.example.core;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.PostConstruct;
import okhttp3.OkHttpClient;
import org.example.entity.BoardInfo;
import org.example.entity.PinInfo;
import org.example.entity.UserInfo;
import org.example.mapper.PinInfoMapper;
import org.example.service.IBoardInfoService;
import org.example.service.IPinInfoService;
import org.example.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @author VNElinpe
 * @since 2023/7/9
 **/
@Component
public class Spider implements ApplicationRunner {
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IBoardInfoService boardInfoService;
    @Autowired
    private IPinInfoService pinInfoService;
    @Value("${huaban.board.per-count}")
    private int boardPerCount;
    @Value("${huaban.board.url-pattern}")
    private String boardUrlPattern;
    @Value("${huaban.pin.per-count}")
    private int pinPerCount;
    @Value("${huaban.pin.url-pattern}")
    private String pinUrlPattern;
    @Value("${huaban.pin.detail-url-pattern}")
    private String detailPinUrlPattern;
    @Value("${huaban.user.ai-url-pattern}")
    private String aiUrlPattern;
    @Value("${huaban.user.discovery-url-pattern}")
    private String discoveryUrlPattern;
    @Value("${huaban.user.ui-url-pattern}")
    private String uiUrlPattern;
    @Value("${threads}")
    private int threads;
    @Value("${date-limit}")
    private String dateLimitStr;
    private LocalDate dateLimit;
    private DefaultUriBuilderFactory uriBuilderFactory;

    @PostConstruct
    private void init() {
        uriBuilderFactory = new DefaultUriBuilderFactory();
        uriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.URI_COMPONENT);
        dateLimit = LocalDate.parse(dateLimitStr);
    }

    public void getAiUser(String filter) {
        HttpHeaders httpHeaders = getHttpHeaders("ai");
        RestTemplateProxy restTemplate = getRestTemplateProxy(3);
        Map<String, Object> params = new HashMap<>();
        for (int i = 1; i <= 10000; i++) {
            params.put("page", i);
            URI expand = uriBuilderFactory.expand(filter, params);
            RequestEntity requestEntity = new RequestEntity<>(null, httpHeaders, HttpMethod.GET, expand);
            JSONObject body = restTemplate.exchange(requestEntity, JSONObject.class).getBody();
            JSONArray pins = body.getJSONArray("pins");
            if (CollectionUtils.isEmpty(pins)) {
                break;
            }
            for (Object pin : pins) {
                JSONObject jpin = JSONObject.from(pin);
                JSONObject user = jpin.getJSONObject("user");
                UserInfo userInfo = new UserInfo();
                userInfo.setUserId(user.getLong("user_id"));
                userInfo.setUserName(user.getString("username"));
                userInfo.setUrlname(user.getString("urlname"));
                userInfo.setType("ai");
                userInfoService.saveOrUpdate(userInfo);
            }
        }
        System.out.println(filter+"finish");
    }

    public void getDiscoveryUser() {
        HttpHeaders httpHeaders = getHttpHeaders("discovery");
        RestTemplateProxy restTemplate = getRestTemplateProxy(3);
        Map<String, Integer> params = new HashMap<>();
        for (int i = 1; i <= 10000; i++) {
            params.put("pageNum", i);
            URI expand = uriBuilderFactory.expand(discoveryUrlPattern, params);
            RequestEntity requestEntity = new RequestEntity<>(null, httpHeaders, HttpMethod.GET, expand);
            JSONObject body = restTemplate.exchange(requestEntity, JSONObject.class).getBody();
            JSONArray pins = body.getJSONArray("pins");
            if (CollectionUtils.isEmpty(pins)) {
                break;
            }
            for (Object pin : pins) {
                JSONObject jpin = JSONObject.from(pin);
                JSONObject user = jpin.getJSONObject("user");
                UserInfo userInfo = new UserInfo();
                userInfo.setUserId(user.getLong("user_id"));
                userInfo.setUserName(user.getString("username"));
                userInfo.setUrlname(user.getString("urlname"));
                userInfo.setType("ai");
                userInfoService.saveOrUpdate(userInfo);
            }
        }
        System.out.println("finish");
    }

    public void getUiUser(String category) {
        HttpHeaders httpHeaders = getHttpHeaders("discovery");
        RestTemplateProxy restTemplate = getRestTemplateProxy(3);
        Map<String, Object> params = new HashMap<>();
        params.put("category", category);
        for (int i = 1; i <= 10000; i++) {
            params.put("pageNum", i);
            URI expand = uriBuilderFactory.expand(uiUrlPattern, params);
            RequestEntity requestEntity = new RequestEntity<>(null, httpHeaders, HttpMethod.GET, expand);
            JSONObject body = restTemplate.exchange(requestEntity, JSONObject.class).getBody();
            JSONArray pins = body.getJSONArray("pins");
            if (CollectionUtils.isEmpty(pins)) {
                break;
            }
            for (Object pin : pins) {
                JSONObject jpin = JSONObject.from(pin);
                JSONObject user = jpin.getJSONObject("user");
                UserInfo userInfo = new UserInfo();
                userInfo.setUserId(user.getLong("user_id"));
                userInfo.setUserName(user.getString("username"));
                userInfo.setUrlname(user.getString("urlname"));
                userInfo.setType(category);
                userInfoService.saveOrUpdate(userInfo);
            }
        }
        System.out.println(category + "finish");
    }

    /**
     * 处理单个用户，用户信息、board信息、pin信息
     * @param urlname urlname
     */
    public void handleUser(String urlname) {
        Map<String, Object> params = new HashMap<>();
        params.put("limit", boardPerCount);
        params.put("urlname", urlname);
        String url = uriBuilderFactory.expand(boardUrlPattern, params).toString();
        Long maxBoardId = null;
        int currentBoardSize = 0;
        int maxBoardSize = 0;
        HttpHeaders httpHeaders = getHttpHeaders(urlname);
        RestTemplateProxy restTemplate = getRestTemplateProxy(3);
        ExecutorService pool = Executors.newFixedThreadPool(4);
        ExecutorService pool4Pin = Executors.newFixedThreadPool(threads);
        do {
            // 获取第一页的board信息
            String currentUrl = url;
            if (maxBoardId != null) {
                currentUrl = String.format("%s&max=%s", url, maxBoardId);
            }
            RequestEntity requestEntity = new RequestEntity<>(null, httpHeaders, HttpMethod.GET, URI.create(currentUrl));
            JSONObject body = getRestTemplateProxy(5).exchange(requestEntity, JSONObject.class).getBody();
            if (maxBoardId == null) {
                // 保存用户信息
                JSONObject user = body.getJSONObject("user");
                saveUserInfo(user);
                maxBoardSize = user.getInteger("board_count");
            }
            // 处理board_info
            JSONArray boards = body.getJSONArray("boards");
            if (CollectionUtils.isEmpty(boards)) {
                break;
            }
            maxBoardId = boards.getJSONObject(boards.size() - 1).getLong("board_id");
            boards.forEach(item -> {
                pool.execute(() -> handleBoard(urlname, JSONObject.from(item), pool4Pin));
            });
            currentBoardSize += boards.size();
        } while (currentBoardSize < maxBoardSize);
        pool.shutdown();
        try {
            pool.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        pool4Pin.shutdown();
        try {
            pool4Pin.awaitTermination(2, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (!pinInfoCache.isEmpty()) {
            try {
                queue.put(pinInfoCache);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            pinInfoCache = new LinkedList<>();
        }
    }

    private final Map<String, AtomicInteger> counter = new ConcurrentHashMap<>();

    private void handleBoard(String urlname, JSONObject board, ExecutorService pool4Pin) {
        saveBoardInfo(board);
        Long boardId = board.getLong("board_id");
        String counterKey = String.format("%s_%s", urlname, boardId);
        counter.put(counterKey, new AtomicInteger());
        Long maxPinId = null;
        int currentPinSize = 0;
        int maxPinSize = board.getInteger("pin_count");
        HttpHeaders httpHeaders = getHttpHeaders(urlname);
        RestTemplateProxy restTemplate = getRestTemplateProxy(5);
        Map<String, Object> params = new HashMap<>();
        params.put("boardId", boardId);
        params.put("limit", pinPerCount);
        String url = uriBuilderFactory.expand(pinUrlPattern, params).toString();
        List<Exception> exceptions = Collections.synchronizedList(new ArrayList<>());
        AtomicBoolean needExit = new AtomicBoolean(false);
        while (currentPinSize < maxPinSize && !needExit.get()) {
            String currentUrl = url;
            if (maxPinId != null) {
                currentUrl = String.format("%s&max=%s", url, maxPinId);
            }
            RequestEntity requestEntity = new RequestEntity<>(null, httpHeaders, HttpMethod.GET, URI.create(currentUrl));
            JSONArray pins = getRestTemplateProxy(5).exchange(requestEntity, JSONObject.class).getBody().getJSONArray("pins");
            if (CollectionUtils.isEmpty(pins)) {
                break;
            }
            maxPinId = pins.getJSONObject(pins.size() - 1).getLong("pin_id");
            for (Object pin : pins) {
                JSONObject jpin = JSONObject.from(pin);
                Long createdAt = jpin.getLong("created_at");
                LocalDate localDate = Instant.ofEpochSecond(createdAt).atOffset(ZoneOffset.ofHours(8)).toLocalDate();
                if (createdAt == null || localDate.isBefore(dateLimit)) {
                    needExit.set(true);
                    break;
                }
                pool4Pin.execute(() -> {
                    if (needExit.get() || counter.get(counterKey).get() > 250000) {
                        needExit.set(true);
                        return;
                    }
                    try {
                        Long pinId = jpin.getLong("pin_id");
                        // 获取pin的详情
                        Map<String, Object> detailParams = new HashMap<>();
                        detailParams.put("pinId", pinId);
                        URI detailUrl = uriBuilderFactory.expand(detailPinUrlPattern, detailParams);
                        RequestEntity detailRequestEntity = new RequestEntity<>(null, getPinHttpHeaders(pinId.toString()), HttpMethod.GET, detailUrl);
                        JSONObject pinDetail = getRestTemplateProxy(3).exchange(detailRequestEntity, JSONObject.class).getBody().getJSONObject("pin");
                        savePinInfo(pinDetail);
                        counter.get(counterKey).getAndIncrement();
                    } catch (Exception e) {
                        e.printStackTrace();
                        exceptions.add(e);
                    }
                });
            }
            currentPinSize += pins.size();
        }
    }

    List<PinInfo> pinInfoCache = new LinkedList<>();
    private final Lock lock = new ReentrantLock();
    BlockingQueue<List<PinInfo>> queue = new ArrayBlockingQueue<>(200);

    private void addCache(PinInfo pinInfo) {
        List<PinInfo> pinInfoCacheBackup = null;
        lock.lock();
        try {
            pinInfoCache.add(pinInfo);
            if (pinInfoCache.size() > 100) {
                pinInfoCacheBackup = pinInfoCache;
                pinInfoCache = new LinkedList<>();
            }
        } finally {
            lock.unlock();
        }
        if (pinInfoCacheBackup != null) {
            try {
                queue.put(pinInfoCacheBackup);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }





    private void savePinInfo(JSONObject pinDetail) {
        if (pinDetail == null) {
            return;
        }
        PinInfo pinInfo = new PinInfo();
        pinInfo.setUserId(pinDetail.getLong("user_id"));
        pinInfo.setBoardId(pinDetail.getLong("board_id"));
        pinInfo.setPinId(pinDetail.getLong("pin_id"));
        pinInfo.setSource(pinDetail.getString("source"));
        pinInfo.setSourceUrl(pinDetail.getString("link"));
        pinInfo.setRawText(pinDetail.getString("raw_text"));
        JSONObject file = pinDetail.getJSONObject("file");
        if (file != null) {
            pinInfo.setFileUrl(String.format("https://gd-hbimg.huaban.com/%s", file.getString("key")));
        }
        pinInfo.setRepinCount(pinDetail.getInteger("repin_count"));
        pinInfo.setLikeCount(pinDetail.getInteger("like_count"));
        JSONObject viaClient = pinDetail.getJSONObject("via_client");
        if (!CollectionUtils.isEmpty(viaClient)) {
            pinInfo.setViaUserId(viaClient.getInteger("via_id"));
            pinInfo.setViaUsername(viaClient.getString("client_name"));
        }
        JSONObject viaUser = pinDetail.getJSONObject("via_user");
        if (!CollectionUtils.isEmpty(viaUser)) {
            pinInfo.setViaUserId(viaUser.getInteger("user_id"));
            pinInfo.setViaUsername(viaUser.getString("username"));
            pinInfo.setViaUrlname(viaUser.getString("urlname"));
            JSONObject avatar = viaUser.getJSONObject("avatar");
            if (avatar != null) {
                pinInfo.setViaUserAvatar(String.format("https://gd-hbimg.huaban.com/%s", avatar.getString("key")));
            }
        }
        pinInfo.setCreatedAt(pinDetail.getLong("created_at"));
        pinInfo.setId(String.format("%s_%s_%s", pinInfo.getUserId(), pinInfo.getBoardId(), pinInfo.getPinId()));
        pinInfo.setRawJson(pinDetail.toJSONString());
//        pinInfoService.saveOrUpdate(pinInfo);
        addCache(pinInfo);
    }

    private void saveBoardInfo(JSONObject board) {
        BoardInfo boardInfo = new BoardInfo();
        boardInfo.setUserId(board.getLong("user_id"));
        boardInfo.setBoardId(board.getLong("board_id"));
        boardInfo.setDescription(board.getString("description"));
        boardInfo.setCategoryId(board.getString("category_id"));
        boardInfo.setTitle(board.getString("title"));
        boardInfo.setFollowCount(board.getInteger("follow_count"));
        boardInfo.setLikeCount(board.getInteger("like_count"));
        boardInfo.setPinCount(board.getInteger("pin_count"));
        boardInfo.setCreatedAt(board.getLong("created_at"));
        boardInfo.setUpdatedAt(board.getLong("updated_at"));
        boardInfo.setId(String.format("%s_%s", boardInfo.getUserId(), boardInfo.getBoardId()));
        boardInfo.setRawJson(board.toJSONString());
        boardInfoService.saveOrUpdate(boardInfo);
    }

    private void saveUserInfo(JSONObject user) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getLong("user_id"));
        userInfo.setUserName(user.getString("username"));
        userInfo.setUrlname(user.getString("urlname"));
        userInfo.setUserFollowingCount(user.getInteger("user_following_count"));
        userInfo.setTagCount(user.getInteger("tag_count"));
        userInfo.setPinCount(user.getInteger("pin_count"));
        userInfo.setBoardCount(user.getInteger("board_count"));
        userInfo.setCreatedAt(user.getLong("created_at"));
        userInfo.setFollowerCount(user.getInteger("follower_count"));
        userInfo.setFollowingCount(user.getInteger("following_count"));
        userInfo.setLikeCount(user.getInteger("like_count"));
        JSONObject profile = user.getJSONObject("profile");
        if (!CollectionUtils.isEmpty(profile)) {
            userInfo.setJob(profile.getString("job"));
            userInfo.setJobName(profile.getString("job_name"));
            userInfo.setJobLocation(profile.getString("location"));
            userInfo.setSex(profile.getString("sex"));
        }
        JSONObject lastLoginGeo = user.getJSONObject("last_login_geo");
        if (!CollectionUtils.isEmpty(lastLoginGeo)) {
            JSONObject geo = lastLoginGeo.getJSONObject("geo");
            if (geo != null) {
                userInfo.setLastLoginLocation(geo.getString("region"));
            }
        }
        JSONObject avatar = user.getJSONObject("avatar");
        if (!CollectionUtils.isEmpty(avatar)) {
            userInfo.setAvatar(String.format("https://gd-hbimg.huaban.com/%s", avatar.getString("key")));
        }
        JSONObject bindings = user.getJSONObject("bindings");
        if (!CollectionUtils.isEmpty(bindings)) {
            try {
                JSONObject qzone = bindings.getJSONObject("qzone");
                if (!CollectionUtils.isEmpty(qzone)) {
                    JSONObject qzoneUserInfo = qzone.getJSONObject("user_info");
                    userInfo.setQzoneUsername(qzoneUserInfo.getString("username"));
                    JSONObject avatar1 = qzoneUserInfo.getJSONObject("avatar");
                    if (avatar1 != null) {
                        userInfo.setQzoneAvatar(String.format("https://gd-hbimg.huaban.com/%s", avatar1.getString("key")));
                    }
                }
                JSONObject weibo = bindings.getJSONObject("weibo");
                if (!CollectionUtils.isEmpty(weibo)) {
                    JSONObject weiboUserInfo = weibo.getJSONObject("user_info");
                    userInfo.setWeiboUsername(weiboUserInfo.getString("username"));
                    JSONObject avatar1 = weiboUserInfo.getJSONObject("avatar");
                    if (avatar1 != null) {
                        userInfo.setWeiboAvatar(String.format("https://gd-hbimg.huaban.com/%s", avatar1.getString("key")));
                    }
                    userInfo.setWeiboAbout(weiboUserInfo.getString("about"));
                    userInfo.setWeiboEmail(weiboUserInfo.getString("email"));
                    userInfo.setWeiboGender(weiboUserInfo.getString("gender"));
                    userInfo.setWeiboLocation(weiboUserInfo.getString("location"));
                    userInfo.setWeiboUrl(weiboUserInfo.getString("url"));
                }
            } catch (Exception e) {
                // ignore
            }
        }
        userInfo.setRawJson(user.toJSONString());
        userInfoService.saveOrUpdate(userInfo);
    }

    @Value("${cookie}")
    private String cookie;

    public HttpHeaders getPinHttpHeaders(String pinId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Origin", "https://huaban.com");
        httpHeaders.set("Sec-Ch-Ua", "\"Not.A/Brand\";v=\"8\", \"Chromium\";v=\"114\", \"Google Chrome\";v=\"114\"");
        httpHeaders.set("Sec-Ch-Ua-Mobile", "?0");
        httpHeaders.set("Sec-Ch-Ua-Platform", "\"Windows\"");
        httpHeaders.set("Sec-Fetch-Dest", "empty");
        httpHeaders.set("Sec-Fetch-Mode", "cors");
        httpHeaders.set("Sec-Fetch-Site", "same-site");
        httpHeaders.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");
        httpHeaders.set("Referer", "https://huaban.com/pins/" + pinId);
        httpHeaders.set("Cookie", cookie);
        return httpHeaders;
    }

    public HttpHeaders getHttpHeaders(String urlname) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Origin", "https://huaban.com");
        httpHeaders.set("Sec-Ch-Ua", "\"Not.A/Brand\";v=\"8\", \"Chromium\";v=\"114\", \"Google Chrome\";v=\"114\"");
        httpHeaders.set("Sec-Ch-Ua-Mobile", "?0");
        httpHeaders.set("Sec-Ch-Ua-Platform", "\"Windows\"");
        httpHeaders.set("Sec-Fetch-Dest", "empty");
        httpHeaders.set("Sec-Fetch-Mode", "cors");
        httpHeaders.set("Sec-Fetch-Site", "same-site");
        httpHeaders.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");
        httpHeaders.set("Referer", "https://huaban.com/user/" + urlname);
        httpHeaders.set("Cookie", cookie);
        return httpHeaders;
    }

    public RestTemplateProxy getRestTemplateProxy(int retryTimes) {
        return new RestTemplateProxy(retryTimes, getRestTemplate());
    }

    private RestTemplate restTemplate = null;

    public RestTemplate getRestTemplate() {
        if (restTemplate == null) {
            synchronized (this) {
                if (restTemplate == null) {
                    // 创建代理
                    SocketAddress sa = new InetSocketAddress("127.0.0.1", 10809);

                    OkHttpClient.Builder builder = new OkHttpClient.Builder();

//                    Proxy proxy = new Proxy(Proxy.Type.HTTP, sa);
//                    builder.proxy(proxy);

                    OkHttpClient client = builder.build();

                    OkHttp3ClientHttpRequestFactory requestFactory = new OkHttp3ClientHttpRequestFactory(client);
                    requestFactory.setConnectTimeout(60000);
                    requestFactory.setReadTimeout(60000);
                    requestFactory.setWriteTimeout(60000);

                    restTemplate = new RestTemplate(requestFactory);
                }
            }
        }
        return restTemplate;
    }

    public static class RestTemplateProxy {
        private final RestTemplate restTemplate;
        private int retryTimes = 3;

        public RestTemplateProxy(int retryTimes, RestTemplate restTemplate) {
            this.retryTimes = retryTimes;
            this.restTemplate = restTemplate;
        }

        public <T> ResponseEntity<T> exchange(RequestEntity<?> requestEntity, Class<T> responseType) throws RestClientException {
            Exception exception = null;
            for (int i = 0; i < retryTimes; i++) {
                try {
                    return restTemplate.exchange(requestEntity, responseType);
                } catch (Exception e) {
                    exception = e;
                    try {
                        TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(800, 2500));
                    } catch (InterruptedException ex) {
                        // ignore
                    }
                } finally {
                    try {
                        TimeUnit.MILLISECONDS.sleep(50);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            throw new RuntimeException(exception);
        }
    }

    @Value("${all-path}")
    private String allPath;
    @Value("${done-path}")
    private String donePathStr;
    @Value("${failed-path}")
    private String failPathStr;
    @Value("${exit-path}")
    private String exitPathStr;
    @Autowired
    private PinInfoMapper pinInfoMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        new Thread(() -> {
            while (true) {
                try {
                    List<PinInfo> take = queue.take();
                    if (!take.isEmpty()) {
                        pinInfoMapper.saveAllWithIngnore(take);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        Path donePath = Paths.get(donePathStr);
        Path failedPath = Paths.get(failPathStr);
        Set<String> done = Files.readAllLines(donePath, StandardCharsets.UTF_8)
                .stream().filter(StringUtils::hasText).collect(Collectors.toSet());
        Set<String> failed = Files.readAllLines(failedPath, StandardCharsets.UTF_8)
                .stream().filter(StringUtils::hasText).collect(Collectors.toSet());
        Set<String> all = Files.readAllLines(Paths.get(allPath), StandardCharsets.UTF_8)
                .stream().filter(StringUtils::hasText)
                .filter(item -> !done.contains(item) && !failed.contains(item)).collect(Collectors.toSet());

        for (String item : all) {
            if (Files.exists(Paths.get(exitPathStr))) {
                System.out.println("exit flag");
                return;
            }
            try {
                handleUser(item);
                Files.writeString(donePath, String.format("%s%s", item, System.lineSeparator()), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("fail! item " + item);
                try {
                    Files.writeString(failedPath, String.format("%s%s", item, System.lineSeparator()), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                } catch (Exception ex) {
                    e.printStackTrace();
                }
            }
        }
    }
}
