package com.example.schoolapp.interfaces

import com.example.schoolapp.activities.NoticeBoardActivity.NoticeBoardRecord


interface OnItemClickListener {
    fun onNoticeClick(notice: NoticeBoardRecord)
}