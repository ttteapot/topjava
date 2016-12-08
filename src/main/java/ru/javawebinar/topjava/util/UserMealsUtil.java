package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );

        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);

//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with correctly exceeded field

        Map<LocalDate, Integer> caloriesMap = new HashMap<>();
        for(UserMeal user:mealList){
            LocalDate userDate = user.getDateTime().toLocalDate();
            if(caloriesMap.containsKey(userDate)){
                caloriesMap.put(userDate ,user.getCalories()+caloriesMap.get(userDate));
            }
            else
            {
                caloriesMap.put(userDate ,user.getCalories());
            }
        }

        return mealList.stream()
                .filter((u)-> TimeUtil.isBetween(u.getDateTime().toLocalTime(), startTime, endTime))
                .map(u->new UserMealWithExceed(u.getDateTime(), u.getDescription(),u.getCalories(),
                        caloriesMap.get(u.getDateTime().toLocalDate())>=caloriesPerDay))
                .sorted((u,u1)-> - u.getDateTime().compareTo(u1.getDateTime()))
                .collect(Collectors.toList());
    }
}
