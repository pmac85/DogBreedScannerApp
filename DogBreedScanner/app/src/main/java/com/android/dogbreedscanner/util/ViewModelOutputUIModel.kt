package com.android.dogbreedscanner.util

/**
 * <prev>
 * Created by Pedro Machado on 2021-04-23.
 * </prev>
 */
sealed class ViewModelOutputUIModel<out Content, out Error, out Empty> {

    object Loading : ViewModelOutputUIModel<Nothing, Nothing, Nothing>()


    data class Content<Content>(val contentData: Content) :
        ViewModelOutputUIModel<Content, Nothing, Nothing>()


    data class Error<Error>(val errorData: Error) :
        ViewModelOutputUIModel<Nothing, Error, Nothing>()


    data class Empty<Empty>(val emptyData: Empty) :
        ViewModelOutputUIModel<Nothing, Nothing, Empty>()
}