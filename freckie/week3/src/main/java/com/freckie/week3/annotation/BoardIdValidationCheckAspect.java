package com.freckie.week3.annotation;

import com.freckie.week3.repository.BoardRepository;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.NoSuchElementException;

@Aspect
public class BoardIdValidationCheckAspect {
    @Autowired
    private BoardRepository boardRepo;

    @Before(value="@annotation(com.freckie.week3.annotation.BoardIdValidationCheck)")
    public void boardIdValidationCheck(ProceedingJoinPoint pjp) throws NoSuchElementException {
        MethodSignature sign = (MethodSignature) pjp.getSignature();
        Method method = sign.getMethod();

        BoardIdValidationCheck check = method.getAnnotation(BoardIdValidationCheck.class);
        String boardId = check.param();
        Long _boardId = Long.valueOf(boardId);

        boardRepo.findById(_boardId).orElseThrow(() -> new NoSuchElementException("No specific board exists"));
    }
}
