package com.tuyano.springboot;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import java.util.logging.Level;
import java.util.logging.Logger;
@Controller
public class ProductController {

	@Autowired
	ResourceLoader resource;

	@RequestMapping(value = "/sample", method = RequestMethod.GET)
	public String sample(HttpServletResponse response) {

		Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		
		logger.log(Level.INFO,"スタート");
		// データ作成
		HashMap<String, Object> params = new HashMap<String, Object>();

		// ヘッダーデータ作成
		params.put("Client_name", "ライフ");
		params.put("Date_today", "令和2年4月24日");

		// フィールドデータ作成
		SampleProductDao dao = new SampleProductDao();
		List<SampleProductModel> fields = dao.findByAll();

		logger.log(Level.INFO,fields.toString());
		
		// データを検索し、帳票を出力
		byte[] output = OrderReporting2(params, fields);

		logger.log(Level.INFO,"出力");
		
		// PDFのダウンロード
		// バイナリファイルの型指定
		response.setContentType("application/octet-stream");
		// ダウンロード時のファイル名
		response.setHeader("Content-Disposition", "attachment; filename=" + "sample.pdf");
		// ダウンロードされるファイルの大きさ
		response.setContentLength(output.length);

		OutputStream os = null;
		try {
			os = response.getOutputStream();
			os.write(output);
			os.flush();

			os.close();
		} catch (IOException e) {
			e.getStackTrace();
		}
		return null;
	}

	/**
	 * JasperReportsのコンパイル
	 * @param param ヘッダーのデータ
	 * @param data　　Daoに格納済みの明細データ
	 * @return　　　　　　バイナリファイル
	 */
	private byte[] OrderReporting2(HashMap<String, Object> param, List<SampleProductModel> data) {

		// バイトを読み込む為のクラス
		InputStream input;

		Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

		
		try {
			// 帳票ファイルを取得
			input = new FileInputStream(resource.getResource("/app/target/classes/report/Blank_A4.jrxml").getFile());
			
			BufferedInputStream bis = new BufferedInputStream(input);
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			int result = bis.read();
			while(result != -1) {
			    buf.write((byte) result);
			    result = bis.read();
			}
			// StandardCharsets.UTF_8.name() > JDK 7
			
			logger.log(Level.INFO,buf.toString("UTF-8"));
			
			//リストをフィールドのデータソースに
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);
			// 帳票をコンパイル
			JasperReport jasperReport = JasperCompileManager.compileReport(input);

			JasperPrint jasperPrint;

			// パラメーターとフィールドデータを注入
			jasperPrint = JasperFillManager.fillReport(jasperReport, param, dataSource);

			// 帳票をByte形式で出力
			return JasperExportManager.exportReportToPdf(jasperPrint);

			
			  } catch (FileNotFoundException e) { // TODO 自動生成された catch ブロック
			  e.printStackTrace(); } catch (IOException e) { // TODO 自動生成された catch ブロック
			  e.printStackTrace();
			 
		} catch (JRException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return null;
	}
}
