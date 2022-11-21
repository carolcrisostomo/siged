package br.com.siged.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;

import br.com.siged.dao.ParticipanteDAO;
import br.com.siged.entidades.Evento;
import br.com.siged.entidades.Modulo;
import br.com.siged.entidades.Nota;
import br.com.siged.entidades.Participante;
import br.com.siged.service.exception.BusinessException;

@Service
public class NotaService {

	@Autowired
	private ParticipanteDAO participanteDao;
	
	/**
	 * Carrega e popula as notas de acordo com arquivo CSV baixado do AVA
	 * 
	 * @param file
	 * @param modulo
	 * @param evento
	 * @param fatorMedia
	 * @return
	 * @throws BusinessException
	 */
	public List<Nota> loadFromCSVFile(MultipartFile file, Modulo modulo, Evento evento, int fatorMedia) throws BusinessException {
		List<Nota> notas = new ArrayList<>();
		
		if(file.isEmpty())
			throw new BusinessException("Nenhum arquivo informado");
			
		if(file.getSize() > 100000l)
			throw new BusinessException("Tamanho do arquivo não suportado");
		
		Map<String, String> notasDoArquivo = lerNotas(file, fatorMedia);
		List<Participante> participantesSemNota = participanteDao.findByEventoIdAndModuloId(evento.getId(), modulo.getId());
		if(participantesSemNota == null || participantesSemNota.isEmpty()) {
			throw new BusinessException("Todas as notas deste módulo já foram atribuídas.");
		}
		
		for(Participante participante : participantesSemNota) {
			if(notasDoArquivo.containsKey(participante.getCpf())) {
				Nota nota = new Nota();
				nota.setEventoId(evento);
				nota.setModuloId(modulo);
				nota.setParticipanteId(participante);
				nota.setNota(notasDoArquivo.get(participante.getCpf()));
				notas.add(nota);
			}
		}
		
		return notas;
	}
	
	/**
	 * Trata as notas para minimizar a influência do usuário na geração do arquivo
	 * 
	 * @param file
	 * @param fatorMedia
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, String> lerNotas(MultipartFile file, int fatorMedia) throws BusinessException {
		int indexCPF = 1;
		int indexNota = 3;
		try {
			Reader reader = new InputStreamReader(file.getInputStream());
			@SuppressWarnings("resource")
			CSVReader csvReader = new CSVReader(reader);
			csvReader.skip(1);
			List<String[]> tuplas = csvReader.readAll();
			
			Map<String, String> notasDoArquivo = new HashMap<>();
			int count = 2;
			for(String[] tupla : tuplas) {
				if(tupla.length > indexNota && tupla[indexCPF] != null && tupla[indexNota] != null) {
					String cpf = tupla[indexCPF].trim();
					String nota = tupla[indexNota].trim();
					
					if(nota.equals("-"))
						nota = "0";
					BigDecimal notaReal;
					try {
						notaReal = new BigDecimal(nota.replace(",", "."));
						if(notaReal.compareTo(BigDecimal.ZERO) == -1)
							throw new BusinessException("Nota menor que 0 na linha " + count);
						
						if(fatorMedia <= 1 && notaReal.compareTo(BigDecimal.TEN) == 1)
							throw new BusinessException("Nota maior que 10 na linha " + count);
						
						if(fatorMedia > 1) {
							BigDecimal divisor = new BigDecimal(fatorMedia);
							notaReal = notaReal.divide(divisor, 1, BigDecimal.ROUND_HALF_UP);
							
							if(notaReal.compareTo(BigDecimal.TEN) == 1)
								throw new BusinessException("Nota maior que 10 na linha " + count);
							
							notasDoArquivo.put(cpf, notaReal.toString().replace(".", ","));
						} else {
							notasDoArquivo.put(cpf, notaReal.setScale(1, BigDecimal.ROUND_HALF_UP).toString().replace(".", ","));
						}
						
					} catch (NumberFormatException e) {
						throw new BusinessException("Não foi possível ler a nota da linha " + count);
					}
					
				} else {
					String erro = String.format("Campo CPF precisa estar na coluna %d e campo NOTA precisa estar na coluna %d", indexCPF + 1, indexNota + 1);
					throw new BusinessException(erro);
				}
				count++;
			}
			
			return notasDoArquivo;
			
		} catch (IllegalStateException | IOException e) {
			throw new BusinessException("Não foi possível ler os dados do arquivo informado");
		}
	}
}
