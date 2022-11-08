package com.jonghae5.jongbirdapi.repository.user.query;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Repository
@Slf4j
@RequiredArgsConstructor
public class UserQueryRepository {
    private final EntityManager em;

}
