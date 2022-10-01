package com.example.quizzy.utilities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.fragment.app.FragmentActivity
import com.example.quizzy.dataModel.entity.Quiz
import com.example.quizzy.dataModel.extras.questionFormat
import com.itextpdf.kernel.geom.Rectangle
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.ColumnDocumentRenderer
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Text
import com.itextpdf.layout.property.TextAlignment
import java.io.File
import java.io.FileNotFoundException
import java.lang.Exception
import java.util.*


class PDF(val activity: FragmentActivity) {


    private val STORAGE_CODE = 1000

    private lateinit var file: File

    @Throws(FileNotFoundException::class)
    fun createQuestion(quiz: Quiz?) {

        try {

            val pdfPath =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString()
            file = File(pdfPath, "${quiz?.title}.pdf")
//        val outputStream:OutputStream = FileOutputStream(file)

            val pdfDocument = PdfDocument(PdfWriter(file))
            val document = Document(pdfDocument)

            val title = Text("${quiz?.title} \n").setBold().setUnderline().setFontSize(12.0f)
            val paragraph = Paragraph().add(title).setTextAlignment(TextAlignment.CENTER)
            document.add(paragraph)
            val columns = arrayOf(
                Rectangle(60.0f, 30.0f, 220.0f, 700.0f),
                Rectangle(320.0f, 30.0f, 220.0f, 700.0f)
            )
            document.setRenderer(ColumnDocumentRenderer(document, columns))

            val questionList: List<questionFormat>? = quiz?.questions?.question
            questionList?.forEachIndexed { index, questionFormat ->
                val index = Text("${index.plus(1)}.").setFontSize(8.0f)
                val question = Text(questionFormat.question + "\n").setFontSize(8.0f)

                val opPara = Paragraph().setMarginLeft(20.0f)
                questionFormat.options?.forEachIndexed { index1, s ->
                    val bullet = Text("o ").setFontSize(11.0f).setBold()
                    val option = Text("$s\n").setFontSize(8.0f)
                    opPara.add(bullet).add(option)
                }

                val qPara = Paragraph().add(index).add(question)
                document.add(qPara)
                document.add(opPara)

            }


            document.close()
            MyToast(activity).showShort("Pdf created,Check documents folder")
            pdfViewer()
        }catch (e:Exception){
            MyToast(activity).showShort("PDF did not save")
        }
    }
    fun createAnswerPDF(ques:List<questionFormat>) {

        try {
            val pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
            file = File(pdfPath, "Answer${UUID.randomUUID()}.pdf")
            val pdfDocument = PdfDocument(PdfWriter(file))
            val document = Document(pdfDocument)


            val para = Paragraph().setMarginLeft(20.0f)
            val questionList: List<questionFormat>? = ques
            questionList?.forEachIndexed { index, questionFormat ->

                val index = Text("${index.plus(1)}.").setFontSize(8.0f)
                val answer = Text("Answer : ${(questionFormat.answer).plus(1)}\n").setFontSize(8.0f).setBold()
                val expHeading=Text("Explanation- ").setFontSize(8.0f).setBold().setUnderline()
                val expl=Text(questionFormat.explanation+"\n")

                para.add(index).add(answer).add(expHeading).add(expl)
                document.add(para)
            }

            document.close()
            MyToast(activity).showShort("Pdf created,Check documents folder")
            pdfViewer()
        }catch (e:Exception){
            MyToast(activity).showShort("Answer pdf did not save")
        }
    }

    private fun pdfViewer() {

        val packageManager = activity.packageManager
        val intent = Intent(Intent.ACTION_VIEW)
        intent.type = "application/pdf"
        val list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        if (list.size > 0) {
            val i = Intent()
            i.action = Intent.ACTION_VIEW
            val uri = Uri.fromFile(file)
            intent.setDataAndType(uri, "application/pdf")
            activity.startActivity(i)
        } else {
            MyToast(activity).showShort("Download a pdf viewer to see generated PDF")
        }

    }

    fun createQuestionPDF(quiz: Quiz?) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                val perms = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                activity.requestPermissions(perms, STORAGE_CODE)
            } else {
                createQuestion(quiz)
            }
        } else {
            createQuestion(quiz)
        }
    }



}