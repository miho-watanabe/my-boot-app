package com.ndd.springboot.controller;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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

import com.ndd.springboot.model.DemoInvoice;
import com.ndd.springboot.model.InvoiceStatement;
import com.ndd.springboot.model.Product;
import com.ndd.springboot.model.SalesforceDepartment;
import com.ndd.springboot.service.DemoInvoiceService;
import com.ndd.springboot.service.ProductService;
import com.ndd.springboot.service.SalesforceDepartmentService;

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
	DemoInvoiceService demoService;
	
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
	 * 請求書一覧画面を表示(POST用)
	 * @param mav
	 * @param email
	 * @param password
	 * @return 請求書一覧ページ
	 */
	@RequestMapping(path = "/list",method = RequestMethod.POST)
	    public ModelAndView sendInvoice(ModelAndView mav,
	    						@RequestParam(name="email",required = false) String email,
	    						@RequestParam(name="password",required = false) String password) {
		
		mav.setViewName("invoice");
		
		List<DemoInvoice> data = demoService.findAll();
		//HerokuConnectで同期をとる場合は↓を実装
		//List<SalesforceDepartment> data = sfService.sfFindAll();
		mav.addObject("data",data);
		return mav;
	    }
	
	/***
	 * 請求書一覧画面を表示(GET用)
	 * @param mav
	 * @return 請求書一覧ページ
	 */
	@RequestMapping(path = "/list",method = RequestMethod.GET)
    public ModelAndView getInvoice(ModelAndView mav) {
		mav.setViewName("invoice");
		List<DemoInvoice> data = demoService.findAll();
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
	 * メールテンプレートページへ遷移
	 * @param mav
	 * @return
	 */
	@RequestMapping(path = "/sendMail",method = RequestMethod.GET)
    public ModelAndView send(ModelAndView mav) {
		mav.setViewName("mailTemplate");
		return mav;
	}
	
	/**
	 * PDFの出力
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/sample", method = RequestMethod.POST)
	public String sample(HttpServletResponse response,
			@RequestParam(name="budgetId",required = false) String budgetId) {

		HashMap<String, Object> params = new HashMap<String, Object>();

		// ロガー
		Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		logger.log(Level.INFO,"これはログのサンプルです");
		logger.log(Level.INFO,"this is sample logs!");

		// ヘッダーデータ作成
		List<DemoInvoice>demodata = demoService.findByBudgetId(budgetId);
		
		params.put("Client_name", demodata.get(0).getAccountName());
		params.put("Date_today", demodata.get(0).getInvoiceDateJapan());
	    params.put("Amount_total", demodata.get(0).getInvoiceAmountTax());
		
		// 社判画像を取得
		InputStream nddImg =null;
		
		try {
			nddImg = new FileInputStream(resource.getResource("classpath:stamp/nddStamp.png").getFile());
		} catch (FileNotFoundException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
		
		params.put("nddStamp", nddImg);
		
		
		// ロゴ画像を取得
		InputStream nddLogo =null;
		
		try {
			nddLogo = new FileInputStream(resource.getResource("classpath:stamp/nddLogo.png").getFile());
		} catch (FileNotFoundException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
		
		params.put("nddLogo", nddLogo);
		
		
		// フィールドデータ作成
		// List<Product>fields = service.findAll();
		
		//明細行の設定
		List<InvoiceStatement> demoStatement = new ArrayList<InvoiceStatement>();
		demoStatement.add(new InvoiceStatement(demodata.get(0).getInvoiceStatement(),demodata.get(0).getInvoiceStatementAmout1()));
		demoStatement.add(new InvoiceStatement(demodata.get(0).getInvoiceStatement2(), demodata.get(0).getInvoiceStatementAmout2()));
		
		//空のデータ作成
		for(int i = 0; i<= 11; i++) {
			demoStatement.add(new InvoiceStatement(null,null));
			/*
			 * Product demo = new Product(); demo.setName(null); demo.setPrice(null);
			 * fields.add(demo);
			 */
		}
		
		// データを検索し、帳票を出力
		byte[] output = OrderReporting2(params, demoStatement);
		
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
	private byte[] OrderReporting2(HashMap<String, Object> param, List<InvoiceStatement> data) {
		
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
