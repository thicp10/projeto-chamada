import re
from pathlib import Path
import xlwt

# Caminhos
ROOT = Path(__file__).resolve().parents[1]
INPUT = ROOT / "alunos.xls"
OUTPUT = ROOT / "alunos_fixed.xls"

# Ler o arquivo binário e decodificar com latin1 para preservar bytes
b = INPUT.read_bytes()
s = b.decode('latin1', errors='ignore')

# Regex para encontrar nomes em caixa alta (sequência de palavras com letras maiúsculas e espaços)
name_pattern = re.compile(r"[A-ZÁÉÍÓÚÃÕÇÂÊÔÀÈÙÝí]+(?:[ \-][A-ZÁÉÍÓÚÃÕÇÂÊÔÀÈÙÝí]+){1,5}")
# Regex para telefones (sequência de dígitos entre 7 e 13)
phone_pattern = re.compile(r"\b\d{7,13}\b")

names = name_pattern.findall(s)
phones = phone_pattern.findall(s)

# Heurística: algumas palavras como WORKBOOK, ROOT, ENTRY aparecem; filtrar nomes muito curtos ou keywords
bad_tokens = set(["WORKBOOK","ROOT","ENTRY","Arial","MbP","thicp","Alunos"])
filtered_names = [n.strip() for n in names if len(n.strip()) > 3 and n.strip() not in bad_tokens]

# Se não encontrarmos nomes suficientes, tentar extrair também palavras com capitalized form
if not filtered_names:
    alt_pattern = re.compile(r"[A-Z][a-z]{2,}(?: [A-Z][a-z]{2,})+")
    filtered_names = alt_pattern.findall(s)

# Emparelhar nomes e telefones
rows = []
max_pairs = max(len(filtered_names), len(phones))
for i in range(max_pairs):
    nome = filtered_names[i] if i < len(filtered_names) else ''
    telefone = phones[i] if i < len(phones) else ''
    rows.append((i+1, False, nome, telefone))

# Se nada foi extraído, criar uma linha de exemplo
if not rows:
    rows = [(1, False, 'Nome Exemplo', '11900000000')]

# Escrever XLS usando xlwt (HSSF)
book = xlwt.Workbook(encoding='utf-8')
sheet = book.add_sheet('Alunos')

headers = ['id','inativo','nome','telefone']
for c,h in enumerate(headers):
    sheet.write(0, c, h)

for r, data in enumerate(rows, start=1):
    sheet.write(r, 0, data[0])
    sheet.write(r, 1, 'sim' if data[1] else 'nao')
    sheet.write(r, 2, data[2])
    sheet.write(r, 3, data[3])

book.save(str(OUTPUT))
print(f"Arquivo gerado: {OUTPUT}")
print(f"Nomes encontrados: {len(filtered_names)}, telefones encontrados: {len(phones)}")

