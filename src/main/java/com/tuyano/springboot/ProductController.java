package com.tuyano.springboot;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author miho.watanabe
 *
 */

@Controller
public class ProductController {

	@Autowired
	ResourceLoader resource;
	@Autowired
	ProductService service;
	@Autowired
	SalesforceDepartmentService sfService;
	
	@Autowired
	@Qualifier("myfile")
	public String myfile;
	
	/**
	 * サインイン画面の表示
	 * @param mav
	 * @return signinページを返却
	 */
	@RequestMapping(path = "/",method = RequestMethod.GET)
    public ModelAndView root(ModelAndView mav) {
		mav.setViewName("signin");
		return mav;
    }
	
	/**
	 * 請求書一覧画面を表示
	 * @param mav
	 * @param email
	 * @param password
	 * @return 請求書一覧ページ
	 */
	@RequestMapping(path = "/",method = RequestMethod.POST)
	    public ModelAndView send(ModelAndView mav,
	    						@RequestParam(name="email",required = false) String email,
	    						@RequestParam(name="password",required = false) String password) {
		
		mav.setViewName("invoice");
		// ヘッダーデータ作成
		List<SalesforceDepartment> data = sfService.sfFindAll();
		mav.addObject("data",data);
		return mav;
	    }

	/**
	 * 検索処理
	 * @param mav
	 * @param txtSearch
	 * @return
	 */
	@RequestMapping(path = "/search",method = RequestMethod.POST)
    public ModelAndView send(ModelAndView mav,
    						@RequestParam(name="txtSearch",required = false) int txtSearch) {
		mav.setViewName("invoice");
		List<SalesforceDepartment> serchData = sfService.findById(txtSearch);
		mav.addObject("data",serchData);
		return mav;
	}
    						
	
	/**
	 * PDFの出力
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/sample", method = RequestMethod.POST)
	public String sample(HttpServletResponse response) {

		HashMap<String, Object> params = new HashMap<String, Object>();

		// ロガー
		Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		logger.log(Level.INFO,"これはログのサンプルです");
		logger.log(Level.INFO,"this is sample logs!");
		// ヘッダーデータ作成
		List<SalesforceDepartment>test = sfService.sfFindAll();
		params.put("Client_name", test.get(0).getName());
		params.put("Date_today", "令和2年4月24日");
		
		// フィールドデータ作成
		List<Product>fields = service.findAll();
		
		// データを検索し、帳票を出力
		byte[] output = OrderReporting2(params, fields);
		
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
	private byte[] OrderReporting2(HashMap<String, Object> param, List<Product> data) {
		
		try {
			// 帳票ファイルを取得
			//input = new FileInputStream(resource.getResource("file:/app/target/classes/report/Blank_A4.jrxml").getFile());
			//input = ClassLoader.getSystemResourceAsStream("Blank_A4.jrxml");
	
			InputStream bais = new ByteArrayInputStream(myfile.getBytes());
			//リストをフィールドのデータソースに
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);
			// 帳票をコンパイル
			JasperReport jasperReport = JasperCompileManager.compileReport(bais);

			JasperPrint jasperPrint;

			// パラメーターとフィールドデータを注入
			jasperPrint = JasperFillManager.fillReport(jasperReport, param, dataSource);

			// 帳票をByte形式で出力
			return JasperExportManager.exportReportToPdf(jasperPrint);

			
			  } catch (JRException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return null;
	}
}
