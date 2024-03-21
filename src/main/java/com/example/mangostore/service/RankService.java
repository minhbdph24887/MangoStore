package com.example.mangostore.service;

import com.example.mangostore.entity.Rank;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

public interface RankService {
    String indexRank(Model model,
                     HttpSession session,
                     String keyword);

    String addRank(Rank addRank,
                   BindingResult result,
                   HttpSession session);

    String detailRank(Model model,
                      HttpSession session,
                      Long idRank);

    String updateRank(BindingResult result,
                      HttpSession session,
                      Rank rank);

    String deleteRank(Long idRank);

    String restoreRank(Long idRank);
}
