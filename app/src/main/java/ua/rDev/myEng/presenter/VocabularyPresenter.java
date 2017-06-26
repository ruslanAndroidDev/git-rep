package ua.rDev.myEng.presenter;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import ua.rDev.myEng.data.MyDataBase;
import ua.rDev.myEng.data.MyDataBaseHelper;
import ua.rDev.myEng.model.Word;
import ua.rDev.myEng.model.WordPack;
import ua.rDev.myEng.view.IVocabulary;

import java.util.ArrayList;

/**
 * Created by pk on 22.05.2017.
 */
@InjectViewState
public class VocabularyPresenter extends MvpPresenter<IVocabulary> {
    Context context;
    static ArrayList<Word> words;

    public VocabularyPresenter() {
    }

    public void loadData(Context context) {
        MyDataBaseHelper.loadWordwithDb(context, new MyDataBaseHelper.DataLoadListener() {

            @Override
            public void onLoad(ArrayList<Word> words) {
                getViewState().showData(words);
                VocabularyPresenter.words = words;
            }
        });
    }

    public void writeWord(String translate, String nativeWord) {
        MyDataBaseHelper.writetodb(context, translate, nativeWord);
        getViewState().insertWord(translate, nativeWord, MyDataBase.STATUS_UNKNOWN);
    }

    public void onWordPackClick(WordPack wordPack) {
        getViewState().showPanel(wordPack);
    }

    public void hidePanel() {
        getViewState().hidePanel();
    }

    public void addWordToBd(WordPack wordPack) {
        for (int i = wordPack.getSize() - 1; i >= 0; i--) {
            if (wordPack.getWordsOriginal(i).isCheck()) {
                if (isUnique(wordPack.getWordsOriginal(i).getWord(), wordPack)) {
                    MyDataBaseHelper.writetodb(context, wordPack.getWordsTranslate(i), wordPack.getWordsOriginal(i).getWord());
                    getViewState().insertWord(wordPack.getWordsTranslate(i), wordPack.getWordsOriginal(i).getWord(), MyDataBase.STATUS_UNKNOWN);
                }
            }
        }
    }

    private boolean isUnique(String word, WordPack wordPack) {
        for (int i = 0; i < words.size(); i++) {
            if (word.equals(words.get(i).getOriginalWord())) {
                return false;
            }
        }
        return true;
    }
}