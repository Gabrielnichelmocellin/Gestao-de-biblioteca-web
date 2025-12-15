const API_BASE_URL = 'http://localhost:8080/api';

function mostrarMensagem(elementoId, texto, tipo = 'erro') {
    const elemento = document.getElementById(elementoId);
    if (!elemento) return;
    
    elemento.textContent = texto;
    elemento.style.color = tipo === 'erro' ? '#e74c3c' : '#27ae60';
    elemento.style.backgroundColor = tipo === 'erro' ? '#fadbd8' : '#d4efdf';
    elemento.style.padding = '10px';
    elemento.style.borderRadius = '5px';
    elemento.style.marginTop = '10px';
    elemento.style.display = 'block';
}

function formatarCPF(cpf) {
    cpf = cpf.replace(/\D/g, '');
    if (cpf.length > 11) cpf = cpf.substring(0, 11);
    
    if (cpf.length <= 3) return cpf;
    if (cpf.length <= 6) return cpf.replace(/(\d{3})(\d+)/, '$1.$2');
    if (cpf.length <= 9) return cpf.replace(/(\d{3})(\d{3})(\d+)/, '$1.$2.$3');
    return cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{1,2})/, '$1.$2.$3-$4');
}

function aplicarMascaraCPF() {
    document.querySelectorAll('input[placeholder*="CPF"], input[id*="cpf"]').forEach(input => {
        input.addEventListener('input', function() {
            this.value = formatarCPF(this.value);
        });
    });
}

async function fazerRequisicao(endpoint, metodo = 'GET', dados = null) {
    let url = endpoint;
  
    if (!url.startsWith('/api')) {
        url = `/api${url}`;
    }
    
    console.log(` ${metodo} ${url}`, dados);
    
    const config = {
        method: metodo,
        headers: {
            'Content-Type': 'application/json',
        }
    };
    
    if (dados && (metodo === 'POST' || metodo === 'PUT' || metodo === 'DELETE')) {
        config.body = JSON.stringify(dados);
    }
    
    try {
        const resposta = await fetch(url, config);
        console.log(` Resposta: ${resposta.status} ${resposta.statusText}`);

        let conteudo;
        try {
            conteudo = await resposta.json();
        } catch (jsonErro) {
            conteudo = await resposta.text();
        }
        
        if (!resposta.ok) {
            throw new Error(`Erro ${resposta.status}: ${JSON.stringify(conteudo)}`);
        }
        
        return conteudo;
    } catch (erro) {
        console.error(` Erro na requisição:`, erro);
        throw erro;
    }
}

function configurarLogin() {
    const btnLogin = document.getElementById('btnLogin');
    if (!btnLogin) return;
    
    btnLogin.addEventListener('click', async function() {
        const usuario = document.getElementById('usuario').value;
        const senha = document.getElementById('senha').value;
        
        if (!usuario || !senha) {
            mostrarMensagem('mensagem', 'Preencha usuário e senha!', 'erro');
            return;
        }
        
        mostrarMensagem('mensagem', 'Login realizado com sucesso!', 'sucesso');
        localStorage.setItem('logado', 'true');
        localStorage.setItem('usuario', usuario);
        
        setTimeout(() => {
            window.location.href = 'dashboard.html';
        }, 1000);
    });
  
    document.addEventListener('keypress', function(event) {
        if (event.key === 'Enter' && document.getElementById('usuario')) {
            btnLogin.click();
        }
    });
}


async function configurarDashboard() {
    console.log('Inicializando dashboard...');
    
    await testarConexaoAPI();
    
    await carregarDados();
    
    configurarEventos();
    
    aplicarMascaraCPF();
    
    const btnSair = document.querySelector('.btn-voltar');
    if (btnSair) {
        btnSair.addEventListener('click', function(e) {
            e.preventDefault();
            localStorage.removeItem('logado');
            localStorage.removeItem('usuario');
            window.location.href = 'index.html';
        });
    }
}

async function testarConexaoAPI() {
    try {
        console.log(' Testando conexão com API...');
        const resposta = await fetch(`${API_BASE_URL}/test`);
        if (resposta.ok) {
            console.log('API conectada com sucesso!');
        } else {
            console.warn('️API retornou erro:', resposta.status);
        }
    } catch (erro) {
        console.error('Não foi possível conectar à API:', erro);
    }
}

async function carregarDados() {
    console.log('Carregando dados...');
   
    try {
        const usuarios = await fazerRequisicao('/usuarios');
        preencherTabelaUsuarios(usuarios);
        console.log(` ${usuarios.length} usuários carregados`);
    } catch (erro) {
        console.error('Erro ao carregar usuários:', erro);
        mostrarMensagem('msgUsuario', 'Erro ao carregar usuários', 'erro');
    }
    
    try {
        const livros = await fazerRequisicao('/livros');
        preencherTabelaLivros(livros);
        console.log(` ${livros.length} livros carregados`);
    } catch (erro) {
        console.error('Erro ao carregar livros:', erro);
        mostrarMensagem('msgLivro', 'Erro ao carregar livros', 'erro');
    }
}

function preencherTabelaUsuarios(usuarios) {
    const tbody = document.querySelector('#tabelaUsuarios tbody');
    if (!tbody) return;
    
    tbody.innerHTML = '';
    
    if (!usuarios || usuarios.length === 0) {
        tbody.innerHTML = '<tr><td colspan="3">Nenhum usuário cadastrado</td></tr>';
        return;
    }
    
    usuarios.forEach(usuario => {
        const linha = `
        <tr>
            <td>${usuario.nome || ''}</td>
            <td>${usuario.cpf || ''}</td>
            <td>${usuario.tipo || ''}</td>
        </tr>`;
        tbody.innerHTML += linha;
    });
}

function preencherTabelaLivros(livros) {
    const tbody = document.querySelector('#tabelaLivros tbody');
    if (!tbody) return;
    
    tbody.innerHTML = '';
    
    if (!livros || livros.length === 0) {
        tbody.innerHTML = '<tr><td colspan="3">Nenhum livro cadastrado</td></tr>';
        return;
    }
    
    livros.forEach(livro => {
        const disponivel = livro.disponivel ? 'Sim' : 'Não';
        const disponivelClasse = livro.disponivel ? 'disponivel' : 'indisponivel';
        
        const linha = `
        <tr>
            <td>${livro.titulo || ''}</td>
            <td>${livro.autor || ''}</td>
            <td class="${disponivelClasse}">${disponivel}</td>
        </tr>`;
        tbody.innerHTML += linha;
    });
}

function configurarEventos() {
    console.log('Configurando eventos...');
    
    const btnSalvarUsuario = document.getElementById('btnSalvarUsuario');
    if (btnSalvarUsuario) {
        btnSalvarUsuario.addEventListener('click', async function() {
            console.log('Salvando usuário...');
            
            const nome = document.getElementById('nomeUsuario').value;
            const cpf = document.getElementById('cpfUsuario').value;
            const tipo = document.getElementById('tipoUsuario').value;
            
            if (!nome || !cpf || !tipo) {
                mostrarMensagem('msgUsuario', 'Preencha todos os campos!', 'erro');
                return;
            }
            
            try {
                const usuario = {
                    nome: nome,
                    cpf: formatarCPF(cpf),
                    tipo: tipo
                };
                
                console.log('Enviando usuário:', usuario);
                const resposta = await fazerRequisicao('/usuarios', 'POST', usuario);
                
                if (resposta.success) {
                    mostrarMensagem('msgUsuario', 'Usuário cadastrado com sucesso!', 'sucesso');
                    await carregarDados(); 
                    
                    setTimeout(() => {
                        document.getElementById('nomeUsuario').value = '';
                        document.getElementById('cpfUsuario').value = '';
                        document.getElementById('tipoUsuario').value = '';
                        document.getElementById('msgUsuario').textContent = '';
                    }, 2000);
                } else {
                    mostrarMensagem('msgUsuario', resposta.message || 'Erro ao cadastrar', 'erro');
                }
            } catch (erro) {
                console.error('Erro:', erro);
                mostrarMensagem('msgUsuario', 'Erro ao conectar com a API', 'erro');
            }
        });

async function devolverEmprestimo(id) {
    console.log(` Tentando devolver empréstimo ID: ${id}`);
    
    if (!confirm(`Deseja registrar a devolução do empréstimo ID ${id}?`)) {
        return;
    }
    
    try {
        const resposta = await fazerRequisicao(`/emprestimos/${id}/devolver`, 'POST');
        
        if (resposta && resposta.success) {
            mostrarMensagem('msgDevolucao', `Devolução registrada com sucesso! ID: ${id}`, 'sucesso');
            
            await carregarEmprestimosAtivos();
            await carregarDevolucoes();
            await carregarLivros();
            
            setTimeout(() => {
                document.getElementById('msgDevolucao').textContent = '';
            }, 2000);
        } else {
            mostrarMensagem('msgDevolucao', ` Erro: ${resposta?.message || 'Resposta inválida'}`, 'erro');
        }
    } catch (erro) {
        console.error(' Erro completo ao devolver empréstimo:', erro);
        mostrarMensagem('msgDevolucao', ` Erro ao devolver: ${erro.message}`, 'erro');
    }
}
}

function configurarGestaoEmprestimos() {
    console.log(' Configurando gestão de empréstimos...');
    
    const btnDevolverLivro = document.getElementById('btnDevolverLivro');
    if (btnDevolverLivro) {
        btnDevolverLivro.addEventListener('click', async function() {
            const idInput = document.getElementById('idEmprestimoDevolver');
            const id = idInput ? parseInt(idInput.value) : null;
            
            if (!id || isNaN(id)) {
                mostrarMensagem('msgDevolucao', ' Digite um ID válido', 'erro');
                return;
            }
            
            await devolverEmprestimo(id);
            
            if (idInput) {
                idInput.value = '';
            }
        });
    }
    
}
    
    const btnSalvarLivro = document.getElementById('btnSalvarLivro');
    if (btnSalvarLivro) {
        btnSalvarLivro.addEventListener('click', async function() {
            console.log(' Salvando livro...');
            
            const titulo = document.getElementById('tituloLivro').value;
            const autor = document.getElementById('autorLivro').value;
            
            if (!titulo || !autor) {
                mostrarMensagem('msgLivro', 'Preencha todos os campos!', 'erro');
                return;
            }
            
            try {
                const livro = {
                    titulo: titulo,
                    autor: autor,
                    disponivel: true
                };
                
                console.log(' Enviando livro:', livro);
                const resposta = await fazerRequisicao('/livros', 'POST', livro);
                
                if (resposta.success) {
                    mostrarMensagem('msgLivro', 'Livro cadastrado com sucesso!', 'sucesso');
                    await carregarDados(); 
                    
                    setTimeout(() => {
                        document.getElementById('tituloLivro').value = '';
                        document.getElementById('autorLivro').value = '';
                        document.getElementById('msgLivro').textContent = '';
                    }, 2000);
                } else {
                    mostrarMensagem('msgLivro', resposta.message || 'Erro ao cadastrar', 'erro');
                }
            } catch (erro) {
                console.error(' Erro:', erro);
                mostrarMensagem('msgLivro', 'Erro ao conectar com a API', 'erro');
            }
        });
    }
    
    const btnEmprestimo = document.getElementById('btnEmprestimo');
    if (btnEmprestimo) {
        btnEmprestimo.addEventListener('click', async function() {
            console.log('Registrando empréstimo...');
            
            const cpf = document.getElementById('cpfEmprestimo').value;
            const livro = document.getElementById('livroEmprestimo').value;
            
            if (!cpf || !livro) {
                mostrarMensagem('msgEmprestimo', 'Preencha todos os campos!', 'erro');
                return;
            }
            
            try {
                const dados = {
                    cpfUsuario: formatarCPF(cpf),
                    tituloLivro: livro
                };
                
                console.log(' Enviando empréstimo:', dados);
                const resposta = await fazerRequisicao('/emprestimos', 'POST', dados);
                
                if (resposta.success) {
                    mostrarMensagem('msgEmprestimo', 'Empréstimo registrado com sucesso!', 'sucesso');
                    await carregarDados();
                    
                    setTimeout(() => {
                        document.getElementById('cpfEmprestimo').value = '';
                        document.getElementById('livroEmprestimo').value = '';
                        document.getElementById('msgEmprestimo').textContent = '';
                    }, 2000);
                } else {
                    mostrarMensagem('msgEmprestimo', resposta.message || 'Erro ao registrar', 'erro');
                }
            } catch (erro) {
                console.error('Erro:', erro);
                mostrarMensagem('msgEmprestimo', 'Erro ao conectar com a API', 'erro');
            }
        });
    }
}

async function carregarEmprestimosAtivos() {
    try {
        const emprestimos = await fazerRequisicao('/emprestimos/ativos');
        preencherTabelaEmprestimosAtivos(emprestimos);
        console.log(` ${emprestimos.length} empréstimos ativos carregados`);
    } catch (erro) {
        console.error(' Erro ao carregar empréstimos ativos:', erro);
        mostrarMensagemGeral('Erro ao carregar empréstimos', 'erro');
    }
}

async function carregarDevolucoes() {
    try {
        const devolucoes = await fazerRequisicao('/devolucoes');
        preencherTabelaDevolucoes(devolucoes);
        console.log(` ${devolucoes.length} devoluções carregadas`);
    } catch (erro) {
        console.error(' Erro ao carregar devoluções:', erro);
     
    }
}

function preencherTabelaEmprestimosAtivos(emprestimos) {
    const tbody = document.querySelector('#tabelaEmprestimosAtivos tbody');
    if (!tbody) return;
    
    tbody.innerHTML = '';
    
    if (!emprestimos || emprestimos.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7"> Nenhum empréstimo ativo no momento</td></tr>';
        return;
    }
    
    emprestimos.forEach(emprestimo => {
        const status = emprestimo.ativo ? 'EMPRESTADO' : 'DEVOLVIDO';
        const statusClasse = emprestimo.ativo ? 'status-emprestado' : 'status-devolvido';
        const dataEmprestimo = formatarData(emprestimo.dataEmprestimo);
        
        const linha = `
        <tr>
            <td><strong>#${emprestimo.id}</strong></td>
            <td>${emprestimo.cpfUsuario || 'N/A'}</td>
            <td>${emprestimo.tituloLivro}</td>
            <td>${dataEmprestimo}</td>
            <td><span class="status-badge ${statusClasse}">${status}</span></td>
            <td class="btn-acoes">
                ${emprestimo.ativo ? `
                    <button class="btn-acao-pequeno btn-devolver" onclick="devolverEmprestimo(${emprestimo.id})">
                        Devolver
                    </button>
                ` : '<span class="status-devolvido">Devolvido</span>'}
            </td>
        </tr>`;
        tbody.innerHTML += linha;
    });
}


function preencherTabelaDevolucoes(devolucoes) {
    const tbody = document.querySelector('#tabelaDevolucoes tbody');
    if (!tbody) return;
    
    tbody.innerHTML = '';
    
    if (!devolucoes || devolucoes.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5">Nenhuma devolução registrada</td></tr>';
        return;
    }
    
    devolucoes.forEach(devolucao => {
        const linha = `
        <tr>
            <td>#${devolucao.id}</td>
            <td>${devolucao.cpfUsuario || 'N/A'}</td>
            <td>${devolucao.tituloLivro}</td>
            <td>${formatarData(devolucao.dataEmprestimo)}</td>
            <td>${formatarData(devolucao.dataDevolucao)}</td>
        </tr>`;
        tbody.innerHTML += linha;
    });
}


function formatarData(dataString) {
    if (!dataString) return 'N/A';
    try {
        const data = new Date(dataString);
        return data.toLocaleDateString('pt-BR') + ' ' + 
               data.toLocaleTimeString('pt-BR').substring(0, 5);
    } catch (e) {
        return dataString;
    }
}

async function devolverEmprestimo(id) {
    if (!confirm(`Deseja registrar a devolução do empréstimo ID ${id}?`)) {
        return;
    }
    
    console.log(` Tentando devolver empréstimo ID: ${id}`);
    
    try {
        const resposta = await fazerRequisicao(`/emprestimos/${id}/devolver`, 'POST');
        console.log('Resposta devolução:', resposta);
        
        if (resposta.success) {
            mostrarMensagemGeral(` Devolução registrada com sucesso!`, 'sucesso');
            
            await carregarEmprestimosAtivos();
            await carregarDevolucoes();
            await carregarLivros();
            
        } else {
            mostrarMensagemGeral(` ${resposta.message}`, 'erro');
        }
    } catch (erro) {
        console.error(' Erro na requisição de devolução:', erro);
        mostrarMensagemGeral(' Erro ao conectar com a API. Verifique o console.', 'erro');
    }
}

function configurarGestaoEmprestimos() {
    const btnDevolverLivro = document.getElementById('btnDevolverLivro');
    if (btnDevolverLivro) {
        btnDevolverLivro.addEventListener('click', async function() {
            const idInput = document.getElementById('idEmprestimoDevolver');
            const id = idInput ? parseInt(idInput.value) : null;
            
            if (!id || isNaN(id)) {
                mostrarMensagem('msgDevolucao', 'Digite um ID válido', 'erro');
                return;
            }
            
            await devolverEmprestimo(id);
            
            if (idInput) {
                idInput.value = '';
            }
        });
    }
    
}

async function configurarDashboard() {
    console.log(' Inicializando dashboard...');
    
    await testarConexaoAPI();
    
    await carregarDados();
    await carregarEmprestimosAtivos();  
    await carregarDevolucoes();       
    
    configurarEventos();
    configurarGestaoEmprestimos();  
    
    aplicarMascaraCPF();
    
    const btnSair = document.querySelector('.btn-voltar');
    if (btnSair) {
        btnSair.addEventListener('click', function(e) {
            e.preventDefault();
            localStorage.removeItem('logado');
            localStorage.removeItem('usuario');
            window.location.href = 'index.html';
        });
    }
}


document.addEventListener('DOMContentLoaded', function() {
    console.log(' Aplicação iniciada');
    
    adicionarEstilosDinamicos();
    
    aplicarMascaraCPF();
    
    if (document.getElementById('btnLogin')) {
        console.log(' Página: Login');
        configurarLogin();
    }
    
    if (document.getElementById('btnSalvarUsuario')) {
        console.log(' Página: Dashboard');
        configurarDashboard();
    }
});

function adicionarEstilosDinamicos() {
    const style = document.createElement('style');
    style.textContent = `
        .disponivel {
            color: #27ae60;
            font-weight: bold;
        }
        
        .indisponivel {
            color: #e74c3c;
            font-weight: bold;
        }
        
        #mensagem, #msgUsuario, #msgLivro, #msgEmprestimo {
            min-height: 20px;
            transition: all 0.3s;
        }
    `;
    document.head.appendChild(style);
}

