package com.tuyano.springboot;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
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
import org.springframework.beans.factory.annotation.Qualifier;
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
	
	@Autowired
	@Qualifier("myfile")
	public String myfile;
	
	@RequestMapping(value = "/sample", method = RequestMethod.GET)
	public String sample(HttpServletResponse response) {

		Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		
		System.out.println("スタート");
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


		
		try {
			// 帳票ファイルを取得
			//input = new FileInputStream(resource.getResource("file:/app/target/classes/report/Blank_A4.jrxml").getFile());
			//input = ClassLoader.getSystemResourceAsStream("Blank_A4.jrxml");
			

			/*
			 * String rtnXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
			 * "<!-- Created with Jaspersoft Studio version 6.12.2.final using JasperReports Library version 6.12.2-75c5e90a222ab406e416cbf590a5397028a52de3  -->\r\n"
			 * +
			 * "<jasperReport xmlns=\"http://jasperreports.sourceforge.net/jasperreports\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd\" name=\"Blank_A4\" pageWidth=\"595\" pageHeight=\"842\" columnWidth=\"555\" leftMargin=\"20\" rightMargin=\"20\" topMargin=\"20\" bottomMargin=\"20\" uuid=\"9eec4c9d-e015-496a-959f-95b5f219696a\">\r\n"
			 * +
			 * "	<property name=\"com.jaspersoft.studio.data.defaultdataadapter\" value=\"One Empty Record\"/>\r\n"
			 * + "	<parameter name=\"Date_today\" class=\"java.lang.String\"/>\r\n" +
			 * "	<parameter name=\"Client_name\" class=\"java.lang.String\"/>\r\n" +
			 * "	<queryString>\r\n" + "		<![CDATA[]]>\r\n" + "	</queryString>\r\n"
			 * + "	<field name=\"product_name\" class=\"java.lang.String\"/>\r\n" +
			 * "	<field name=\"price\" class=\"java.lang.Integer\"/>\r\n" +
			 * "	<variable name=\"product_name1\" class=\"java.lang.Integer\" resetType=\"Page\" calculation=\"Count\">\r\n"
			 * +
			 * "		<variableExpression><![CDATA[$F{product_name}]]></variableExpression>\r\n"
			 * + "	</variable>\r\n" +
			 * "	<variable name=\"price1\" class=\"java.lang.Integer\" resetType=\"Page\" calculation=\"Count\">\r\n"
			 * + "		<variableExpression><![CDATA[$F{price}]]></variableExpression>\r\n"
			 * + "	</variable>\r\n" +
			 * "	<variable name=\"product_name2\" class=\"java.lang.Integer\" resetType=\"Page\" calculation=\"DistinctCount\">\r\n"
			 * +
			 * "		<variableExpression><![CDATA[$F{product_name}]]></variableExpression>\r\n"
			 * + "	</variable>\r\n" +
			 * "	<variable name=\"price2\" class=\"java.lang.Integer\" resetType=\"Page\" calculation=\"DistinctCount\">\r\n"
			 * + "		<variableExpression><![CDATA[$F{price}]]></variableExpression>\r\n"
			 * + "	</variable>\r\n" + "	<background>\r\n" +
			 * "		<band splitType=\"Stretch\"/>\r\n" + "	</background>\r\n" +
			 * "	<title>\r\n" + "		<band height=\"88\" splitType=\"Stretch\">\r\n"
			 * + "			<textField>\r\n" +
			 * "				<reportElement x=\"200\" y=\"0\" width=\"162\" height=\"30\" uuid=\"24c14a3c-d342-4c3e-a091-f476202a2806\"/>\r\n"
			 * +
			 * "				<textElement textAlignment=\"Center\" verticalAlignment=\"Middle\"/>\r\n"
			 * +
			 * "				<textFieldExpression><![CDATA[\"商品一覧表\"]]></textFieldExpression>\r\n"
			 * + "			</textField>\r\n" + "			<textField>\r\n" +
			 * "				<reportElement x=\"10\" y=\"50\" width=\"100\" height=\"30\" uuid=\"80d7784c-882f-462f-bb52-9f79859c0496\"/>\r\n"
			 * + "				<textElement>\r\n" +
			 * "					<font fontName=\"IPAexg\"/>\r\n" +
			 * "				</textElement>\r\n" +
			 * "				<textFieldExpression><![CDATA[$P{Client_name}]]></textFieldExpression>\r\n"
			 * + "			</textField>\r\n" + "			<textField>\r\n" +
			 * "				<reportElement x=\"450\" y=\"50\" width=\"100\" height=\"30\" uuid=\"ac85d7fa-30e3-4b13-98b1-3c883e90f028\"/>\r\n"
			 * + "				<textElement>\r\n" +
			 * "					<font fontName=\"IPAexg\"/>\r\n" +
			 * "				</textElement>\r\n" +
			 * "				<textFieldExpression><![CDATA[$P{Date_today}]]></textFieldExpression>\r\n"
			 * + "			</textField>\r\n" + "		</band>\r\n" + "	</title>\r\n" +
			 * "	<pageHeader>\r\n" +
			 * "		<band height=\"91\" splitType=\"Stretch\">\r\n" +
			 * "			<property name=\"com.jaspersoft.studio.layout\" value=\"com.jaspersoft.studio.editor.layout.HorizontalRowLayout\"/>\r\n"
			 * + "		</band>\r\n" + "	</pageHeader>\r\n" + "	<columnHeader>\r\n" +
			 * "		<band height=\"43\" splitType=\"Stretch\">\r\n" +
			 * "			<property name=\"com.jaspersoft.studio.layout\" value=\"com.jaspersoft.studio.editor.layout.grid.JSSGridBagLayout\"/>\r\n"
			 * + "			<staticText>\r\n" +
			 * "				<reportElement x=\"0\" y=\"0\" width=\"278\" height=\"43\" uuid=\"c7adc3a1-9245-4953-a404-6a41779260a6\"/>\r\n"
			 * + "				<box>\r\n" +
			 * "					<topPen lineWidth=\"1.0\"/>\r\n" +
			 * "					<leftPen lineWidth=\"1.0\"/>\r\n" +
			 * "					<bottomPen lineWidth=\"1.0\"/>\r\n" +
			 * "					<rightPen lineWidth=\"1.0\"/>\r\n" +
			 * "				</box>\r\n" +
			 * "				<textElement textAlignment=\"Center\" verticalAlignment=\"Middle\">\r\n"
			 * + "					<font fontName=\"IPAexg\"/>\r\n" +
			 * "				</textElement>\r\n" +
			 * "				<text><![CDATA[商品名]]></text>\r\n" +
			 * "			</staticText>\r\n" + "			<staticText>\r\n" +
			 * "				<reportElement x=\"278\" y=\"0\" width=\"277\" height=\"43\" uuid=\"9fdee941-2772-4b07-9c78-ba61f732be14\"/>\r\n"
			 * + "				<box>\r\n" +
			 * "					<topPen lineWidth=\"1.0\"/>\r\n" +
			 * "					<leftPen lineWidth=\"1.0\"/>\r\n" +
			 * "					<bottomPen lineWidth=\"1.0\"/>\r\n" +
			 * "					<rightPen lineWidth=\"1.0\"/>\r\n" +
			 * "				</box>\r\n" +
			 * "				<textElement textAlignment=\"Center\" verticalAlignment=\"Middle\">\r\n"
			 * + "					<font fontName=\"IPAexg\"/>\r\n" +
			 * "				</textElement>\r\n" +
			 * "				<text><![CDATA[価格]]></text>\r\n" +
			 * "			</staticText>\r\n" + "		</band>\r\n" +
			 * "	</columnHeader>\r\n" + "	<detail>\r\n" +
			 * "		<band height=\"125\" splitType=\"Stretch\">\r\n" +
			 * "			<textField>\r\n" +
			 * "				<reportElement x=\"0\" y=\"0\" width=\"278\" height=\"30\" uuid=\"44d12e32-5c75-4b83-a9a4-6040766e6daf\">\r\n"
			 * +
			 * "					<property name=\"com.jaspersoft.studio.spreadsheet.connectionID\" value=\"75d7a781-aeae-480a-9af0-56632e393f89\"/>\r\n"
			 * + "				</reportElement>\r\n" + "				<box>\r\n" +
			 * "					<topPen lineWidth=\"1.0\"/>\r\n" +
			 * "					<leftPen lineWidth=\"1.0\"/>\r\n" +
			 * "					<bottomPen lineWidth=\"1.0\"/>\r\n" +
			 * "					<rightPen lineWidth=\"1.0\"/>\r\n" +
			 * "				</box>\r\n" +
			 * "				<textElement textAlignment=\"Center\" verticalAlignment=\"Middle\">\r\n"
			 * + "					<font fontName=\"IPAexg\"/>\r\n" +
			 * "				</textElement>\r\n" +
			 * "				<textFieldExpression><![CDATA[$F{product_name}]]></textFieldExpression>\r\n"
			 * + "			</textField>\r\n" +
			 * "			<textField pattern=\"¤#,##0.##;¤-#,##0.##\">\r\n" +
			 * "				<reportElement x=\"278\" y=\"0\" width=\"277\" height=\"30\" uuid=\"cf3b8336-b878-4ad5-94e7-1007b53b00c3\">\r\n"
			 * +
			 * "					<property name=\"com.jaspersoft.studio.spreadsheet.connectionID\" value=\"78f137fb-42a1-4225-bea8-12ecf6d6e8c9\"/>\r\n"
			 * + "				</reportElement>\r\n" + "				<box>\r\n" +
			 * "					<topPen lineWidth=\"1.0\"/>\r\n" +
			 * "					<leftPen lineWidth=\"1.0\"/>\r\n" +
			 * "					<bottomPen lineWidth=\"1.0\"/>\r\n" +
			 * "					<rightPen lineWidth=\"1.0\"/>\r\n" +
			 * "				</box>\r\n" +
			 * "				<textElement textAlignment=\"Center\" verticalAlignment=\"Middle\">\r\n"
			 * + "					<font fontName=\"IPAexg\"/>\r\n" +
			 * "				</textElement>\r\n" +
			 * "				<textFieldExpression><![CDATA[$F{price}]]></textFieldExpression>\r\n"
			 * + "			</textField>\r\n" + "		</band>\r\n" + "	</detail>\r\n" +
			 * "	<columnFooter>\r\n" +
			 * "		<band height=\"45\" splitType=\"Stretch\"/>\r\n" +
			 * "	</columnFooter>\r\n" + "	<pageFooter>\r\n" +
			 * "		<band height=\"54\" splitType=\"Stretch\"/>\r\n" +
			 * "	</pageFooter>\r\n" + "	<summary>\r\n" +
			 * "		<band height=\"42\" splitType=\"Stretch\"/>\r\n" +
			 * "	</summary>\r\n" + "</jasperReport>\r\n" + "";
			 */	
			
			//InputStream bais = new ByteArrayInputStream(myfile.getBytes("utf-8"));
			
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
